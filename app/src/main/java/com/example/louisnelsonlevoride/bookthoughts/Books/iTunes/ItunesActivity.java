package com.example.louisnelsonlevoride.bookthoughts.Books.iTunes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.louisnelsonlevoride.bookthoughts.Books.AddEditBookActivity;
import com.example.louisnelsonlevoride.bookthoughts.Books.CustomBook.CustomBookInterface;
import com.example.louisnelsonlevoride.bookthoughts.DialogProgressBar;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseItunesBook;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseItunesSearch;
import com.example.louisnelsonlevoride.bookthoughts.NavigationActivity;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.BookClient;
import com.example.louisnelsonlevoride.bookthoughts.Services.ItunesClient;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;
import com.example.louisnelsonlevoride.bookthoughts.ErrorToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItunesActivity extends AppCompatActivity implements CustomBookInterface,SelectItunesBookInterface {

    private RecyclerView recyclerView;
    private ItunesAdapter itunesAdapter;
    private String TAG = "ItunesActivity";
    private String title;
    private String searchTitle;
    private String author;
    private String username;
    private DialogProgressBar dialogProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itunes);
        setTitle(getString(R.string.itunes_books));
        recyclerView = findViewById(R.id.itunes_recycle_view);
        title = getIntent().getStringExtra("title");
        searchTitle = title.replaceFirst("\\s++$", "").replaceAll(" ", "+").toLowerCase();
        author = getIntent().getStringExtra("author");
        username = CurrentUserSingleton.getInstance().getUsername(this);
        dialogProgressBar = new DialogProgressBar(this);
        dialogProgressBar.setProgressTextView(getString(R.string.please_wait));
        searchForBooks();
    }

    private void searchForBooks(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ItunesClient client = retrofit.create(ItunesClient.class);
        Call<ResponseItunesSearch> call = client.getBooks(searchTitle);
        call.enqueue(new Callback<ResponseItunesSearch>() {
            @Override
            public void onResponse(Call<ResponseItunesSearch> call, Response<ResponseItunesSearch> response) {
                dialogProgressBar.hideDialog();
                if (response.isSuccessful() && (response.body()!=null)){
                    ResponseItunesSearch responseItunesSearch = response.body();
                    if (responseItunesSearch.getBooks() != null){
                        recyclerView = findViewById(R.id.itunes_recycle_view);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ItunesActivity.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(new ItunesAdapter(ItunesActivity.this,responseItunesSearch.getBooks(),ItunesActivity.this,ItunesActivity.this));
                    }else{
                        new ErrorToast().showErrorMessage(ItunesActivity.this);
                    }
                }else{
                    new ErrorToast().showErrorMessage(ItunesActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseItunesSearch> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(ItunesActivity.this);
            }
        });
    }

    @Override
    public void transitionToCustomActivity() {
        Intent intent = new Intent(this, AddEditBookActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("author", author);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
    }

    @Override
    public void selectBook(ResponseItunesBook responseItunesBook) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/book/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        BookClient client = retrofit.create(BookClient.class);
        Call<Book> call = client.createBookWithImageUrl(username,
                responseItunesBook.getTrackName(),
                responseItunesBook.getArtistName(),
                responseItunesBook.getArtworkUrl100());
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                dialogProgressBar.hideDialog();
                if (response.body() != null){
                    Intent intent = new Intent(ItunesActivity.this, NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent,1);
                }else {
                    new ErrorToast().showErrorMessage(ItunesActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(ItunesActivity.this);
            }
        });
    }
}
