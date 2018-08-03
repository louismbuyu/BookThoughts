package com.example.louisnelsonlevoride.bookthoughts.Books.Chapters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.louisnelsonlevoride.bookthoughts.Books.AddEditBookActivity;
import com.example.louisnelsonlevoride.bookthoughts.Books.Quotes.QuotesActivity;
import com.example.louisnelsonlevoride.bookthoughts.Books.SelectBookInterface;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.Models.Chapter;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseChapters;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.ChapterClient;
import com.example.louisnelsonlevoride.bookthoughts.ErrorToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChaptersActivity extends AppCompatActivity implements SelectChapterInterface,SelectBookInterface,AddChapterInterface {

    RecyclerView recyclerView;
    List<Chapter> chapters;
    Book book;
    private String TAG = "ChaptersActivity";
    SelectChapterInterface selectChapterInterface;
    SelectBookInterface selectBookInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        recyclerView = findViewById(R.id.chapters_recycle_view);
        setTitle(getString(R.string.chapters));
        Book tempBook = getIntent().getParcelableExtra("book");
        if (tempBook!=null){
            book = tempBook;
        }

        if (!book.getId().isEmpty()){
            getChapters();
        }else{
            new ErrorToast().showErrorMessage(ChaptersActivity.this);
        }
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getChapters(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/chapter/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ChapterClient client = retrofit.create(ChapterClient.class);
        Call<ResponseChapters> call = client.getBooksChapters(book.getId());
        call.enqueue(new Callback<ResponseChapters>() {
            @Override
            public void onResponse(Call<ResponseChapters> call, Response<ResponseChapters> response) {
                if (response.body() != null && response.body().getSuccess()){
                    chapters = response.body().getChapters();
                    setupView();
                }else{
                    new ErrorToast().showErrorMessage(ChaptersActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseChapters> call, Throwable t) {
                new ErrorToast().showErrorMessage(ChaptersActivity.this);
            }
        });
    }

    private void setupView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        ChaptersAdapter chaptersAdapter = new ChaptersAdapter(this,chapters,book,this,this,this);
        recyclerView.setAdapter(chaptersAdapter);
    }

    @Override
    public void selectBook(int position) {

    }

    @Override
    public void selectEditBook() {
        Intent intent = new Intent(this,AddEditBookActivity.class);
        intent.putExtra("book",book);
        intent.putExtra("type",false);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_book_item:
                transitionToAddEditChapter();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addNewChapter() {
        transitionToAddEditChapter();
    }

    private void transitionToAddEditChapter(){
        Intent intent = new Intent(this,AddEditChapterActivity.class);
        intent.putExtra("book",book);
        startActivityForResult(intent,1);
    }

    @Override
    public void selectChapter(int position) {
        Intent intent = new Intent(this, QuotesActivity.class);
        intent.putExtra("book",book);
        intent.putExtra("chapter",chapters.get(position));
        startActivityForResult(intent,1);
    }

    @Override
    public void editChapter() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getChapters();
        }
    }
}
