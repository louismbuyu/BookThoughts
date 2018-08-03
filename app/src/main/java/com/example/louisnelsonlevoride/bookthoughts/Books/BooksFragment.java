package com.example.louisnelsonlevoride.bookthoughts.Books;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.example.louisnelsonlevoride.bookthoughts.BookThoughtWidget;
import com.example.louisnelsonlevoride.bookthoughts.Books.Chapters.ChaptersActivity;
import com.example.louisnelsonlevoride.bookthoughts.DBData.AppExecutors;
import com.example.louisnelsonlevoride.bookthoughts.DBData.BookDatabase;
import com.example.louisnelsonlevoride.bookthoughts.DialogProgressBar;
import com.example.louisnelsonlevoride.bookthoughts.DialogSuccess;
import com.example.louisnelsonlevoride.bookthoughts.MainViewModel;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.Models.Chapter;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseBooks;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.BookClient;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BooksFragment extends Fragment implements SelectBookInterface,DialogInterface {

    RecyclerView recyclerView;
    BooksAdapter booksAdapter;
    List<Book> books;
    List<Chapter> chapters;
    private String TAG = "BooksFragment";
    private String userId;
    private Boolean OnceOff = true;
    DialogSuccess dialogSuccess;
    private BookDatabase mDb;

    public BooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_books, container, false);
        userId = CurrentUserSingleton.getInstance().getUserId(getContext());
        mDb = BookDatabase.getInstance(getActivity());
        recyclerView = rootView.findViewById(R.id.books_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        booksAdapter = new BooksAdapter(getActivity(),books,this,this);
        recyclerView.setAdapter(booksAdapter);
        getActivity().setTitle(getString(R.string.books));
        getBooksFromDatabase();
        return rootView;
    }

    private void addBooksToDatabase(final List<Book> books){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.BookDao().insertBooks(books);
            }
        });
    }

    private void getBooksFromDatabase(){

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (viewModel != null){
            LiveData<List<Book>> listLiveData = viewModel.getBooks();
            if (listLiveData != null){
                listLiveData.observe(this, new Observer<List<Book>>() {
                    @Override
                    public void onChanged(@Nullable List<Book> newBooks) {
                        if (newBooks.size() == 0){
                            getBooks();
                        }else{
                            books = newBooks;
                            booksAdapter = new BooksAdapter(getActivity(),books,BooksFragment.this,BooksFragment.this);
                            recyclerView.setAdapter(booksAdapter);
                            getNewBooks(newBooks.get(0).getTimeStamp());
                        }
                    }
                });
            }
        }

        /*BookDatabase database = BookDatabase.getInstance(getActivity());
        LiveData<List<Book>> tempBooks = database.BookDao().loadAllBooks();
        if (tempBooks != null){
            tempBooks.observe(getActivity(), new Observer<List<Book>>() {
                @Override
                public void onChanged(@Nullable final List<Book> newBooks) {
                    if (newBooks != null){
                        if (newBooks.size() == 0){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getBooks();
                                }
                            });
                        }else{
                            books = newBooks;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    booksAdapter = new BooksAdapter(getActivity(),books,BooksFragment.this,BooksFragment.this);
                                    recyclerView.setAdapter(booksAdapter);
                                    getNewBooks(newBooks.get(0).getTimeStamp());
                                }
                            });
                        }
                    }
                }
            });
        }else {
            Log.i(TAG,"NO BOOKS IN DATABASE");
        }*/
    }

    private void getBooks(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/book/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        BookClient client = retrofit.create(BookClient.class);
        Call<ResponseBooks> call = client.getUsersBooks(userId);
        call.enqueue(new Callback<ResponseBooks>() {
            @Override
            public void onResponse(Call<ResponseBooks> call, final Response<ResponseBooks> response) {
                if (response.body() != null && response.body().getSuccess()){

                    if (response.body().getBooks().size() != 0){
                        addBooksToDatabase(response.body().getBooks());
                        getBooksFromDatabase();
                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<ResponseBooks> call, Throwable t) {
                Log.i(TAG,"Error: "+t.getLocalizedMessage());
            }
        });
    }

    private void getNewBooks(String lastTimeStamp){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/book/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        BookClient client = retrofit.create(BookClient.class);
        Call<ResponseBooks> call = client.getUsersBooksWithFilter(userId,lastTimeStamp);
        call.enqueue(new Callback<ResponseBooks>() {
            @Override
            public void onResponse(Call<ResponseBooks> call, final Response<ResponseBooks> response) {
                if (response.body() != null && response.body().getSuccess()){
                    if (response.body().getBooks().size() != 0){
                        addBooksToDatabase(response.body().getBooks());
                        //getBooksFromDatabase();
                    }
                }else{

                }
            }
            @Override
            public void onFailure(Call<ResponseBooks> call, Throwable t) {
                Log.i(TAG,"Error: "+t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void selectBook(int position) {
        Book book = books.get(position);
        Intent intent = new Intent(getActivity(), ChaptersActivity.class);
        intent.putExtra("book",book);
        getActivity().startActivityForResult(intent,1);
    }

    @Override
    public void selectEditBook() {
    }

    @Override
    public void showDialog(Book book) {
        dialogSuccess = new DialogSuccess(getActivity());
        String firstPart = getString(R.string.you_have_successfully_saved);
        String secondPart = getString(R.string.fav_book_is_viewable);
        String placeHolder = firstPart+"  "+book.getTitle()+"!\n"+secondPart;
        dialogSuccess.successDescription.setText(placeHolder);
        dialogSuccess.showDialog(true);
        dialogSuccess.successBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSuccess.dialog.dismiss();
                getBooksFromDatabase();
                updateWidget();
            }
        });
    }

    private void updateWidget(){
        Intent intent = new Intent(getActivity(), BookThoughtWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), BookThoughtWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intent);
    }
}
