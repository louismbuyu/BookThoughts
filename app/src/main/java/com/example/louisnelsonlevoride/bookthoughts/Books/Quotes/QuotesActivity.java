package com.example.louisnelsonlevoride.bookthoughts.Books.Quotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.louisnelsonlevoride.bookthoughts.Books.Chapters.AddEditChapterActivity;
import com.example.louisnelsonlevoride.bookthoughts.Books.Chapters.ChaptersAdapter;
import com.example.louisnelsonlevoride.bookthoughts.Books.Chapters.SelectChapterInterface;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.Models.Chapter;
import com.example.louisnelsonlevoride.bookthoughts.Models.Quote;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseChapters;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseQuotes;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.ChapterClient;
import com.example.louisnelsonlevoride.bookthoughts.Services.QuoteClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuotesActivity extends AppCompatActivity implements SelectChapterInterface,SelectQuoteInterface,AddQuoteInterface {

    RecyclerView recyclerView;
    List<Quote> quotes;
    Book book;
    Chapter chapter;
    private String TAG = "QuotesActivity";
    Boolean refresh;
    QuotesAdapter quotesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        recyclerView = findViewById(R.id.quotes_recycle_view);
        Book tempBook = getIntent().getParcelableExtra("book");
        if (tempBook != null){
            book = tempBook;
        }

        Chapter tempChapter = getIntent().getParcelableExtra("chapter");
        if (tempChapter!=null){
            chapter = tempChapter;
            setTitle(R.string.chapter+" "+chapter.getNumber());
            getQuotesForChapter(chapter.get_id());
        }

    }

    private void getQuotesForChapter(String chapterId){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/quote/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        QuoteClient client = retrofit.create(QuoteClient.class);
        Call<ResponseQuotes> call = client.getQuotes(chapterId);
        call.enqueue(new Callback<ResponseQuotes>() {
            @Override
            public void onResponse(Call<ResponseQuotes> call, Response<ResponseQuotes> response) {
                if (response.body() != null && response.body().getSuccess()){
                    quotes = response.body().getQuotes();
                    setupView();
                }else{

                }
            }

            @Override
            public void onFailure(Call<ResponseQuotes> call, Throwable t) {
            }
        });
    }

    private void setupView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        quotesAdapter = new QuotesAdapter(this,quotes,this,this,this,chapter);
        recyclerView.setAdapter(quotesAdapter);
    }

    @Override
    public void selectChapter(int position) {
        //No action here

    }

    @Override
    public void editChapter() {
        Intent intent =new Intent(this, AddEditChapterActivity.class);
        intent.putExtra("chapter",chapter);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
    }

    @Override
    public void selectQuote(int position) {
        Intent intent = new Intent(this,DisplayQuoteActivity.class);
        intent.putExtra("book",book);
        intent.putExtra("chapter",chapter);
        intent.putExtra("quote",quotes.get(position));
        startActivityForResult(intent,1);
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
                transitionToAddQuote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void transitionToAddQuote(){
        Intent intent = new Intent(this,AddEditQuoteActivity.class);
        intent.putExtra("book",book);
        intent.putExtra("chapter",chapter);
        startActivityForResult(intent,1);
    }

    @Override
    public void addNewQuote() {
        transitionToAddQuote();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getQuotesForChapter(chapter.get_id());
        }
    }
}
