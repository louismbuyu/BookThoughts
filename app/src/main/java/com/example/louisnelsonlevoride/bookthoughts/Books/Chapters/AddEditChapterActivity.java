package com.example.louisnelsonlevoride.bookthoughts.Books.Chapters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.louisnelsonlevoride.bookthoughts.DialogProgressBar;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.Models.Chapter;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreate;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.ChapterClient;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;
import com.example.louisnelsonlevoride.bookthoughts.ErrorToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEditChapterActivity extends AppCompatActivity {

    EditText chapterTitleEditText;
    EditText chapterNumberEditText;
    Button saveBtn;
    Button deleteBtn;
    Book book;
    Chapter chapter;
    Boolean update = false;
    private String TAG = "AddEditChapterActivity";
    String username;
    private DialogProgressBar dialogProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_chapter);
        chapterTitleEditText = findViewById(R.id.chapter_title_edit_text);
        chapterNumberEditText = findViewById(R.id.chapter_number_edit_text);
        saveBtn = findViewById(R.id.chapter_save_btn);
        deleteBtn = findViewById(R.id.chapter_delete_btn);
        dialogProgressBar = new DialogProgressBar(this);
        dialogProgressBar.setProgressTextView(getString(R.string.please_wait));

        Book tempBook = getIntent().getParcelableExtra("book");
        if (tempBook!=null){
            book = tempBook;
        }

        Chapter tempChapter = getIntent().getParcelableExtra("chapter");
        if (tempChapter!=null){
            chapter = tempChapter;
            update = true;
            chapterTitleEditText.setText(chapter.getTitle());
            String placeHolder = ""+chapter.getNumber();
            chapterNumberEditText.setText(placeHolder);
            deleteBtn.setVisibility(View.VISIBLE);
        }else{
            deleteBtn.setVisibility(View.INVISIBLE);
        }

        username = CurrentUserSingleton.getInstance().getUsername(this);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!chapterTitleEditText.getText().toString().isEmpty() && !chapterNumberEditText.getText().toString().isEmpty()){
                    if (update){
                        updateChapter(chapter.get_id(),chapterTitleEditText.getText().toString(),Integer.parseInt(chapterNumberEditText.getText().toString()));
                    }else {
                        createNewChapter(book.getId(),username,chapterTitleEditText.getText().toString(),Integer.parseInt(chapterNumberEditText.getText().toString()));
                    }
                }else{
                    Toast.makeText(AddEditChapterActivity.this, R.string.please_enter_title_number,Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteChapter(chapter.get_id());
            }
        });

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void createNewChapter(String bookId, String username, String title, int number){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/chapter/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ChapterClient client = retrofit.create(ChapterClient.class);
        Call<ResponseCreate> call = client.createChapter(bookId,username,title,number);
        call.enqueue(new Callback<ResponseCreate>() {
            @Override
            public void onResponse(Call<ResponseCreate> call, Response<ResponseCreate> response) {
                dialogProgressBar.hideDialog();
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().getSuccess()){
                        Intent intent = new Intent(AddEditChapterActivity.this, ChaptersActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent,1);
                    }else{
                        new ErrorToast().showErrorMessage(AddEditChapterActivity.this);
                    }
                }else{
                    new ErrorToast().showErrorMessage(AddEditChapterActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreate> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(AddEditChapterActivity.this);
            }
        });
    }

    private void updateChapter(String chapterId, String title, int number){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/chapter/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ChapterClient client = retrofit.create(ChapterClient.class);
        Call<ResponseCreate> call = client.updateChapter(chapterId,title,number);
        call.enqueue(new Callback<ResponseCreate>() {
            @Override
            public void onResponse(Call<ResponseCreate> call, Response<ResponseCreate> response) {
                dialogProgressBar.hideDialog();
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().getSuccess()){
                        Intent intent = new Intent(AddEditChapterActivity.this, ChaptersActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent,1);
                    }else{
                        new ErrorToast().showErrorMessage(AddEditChapterActivity.this);
                    }
                }else{
                    new ErrorToast().showErrorMessage(AddEditChapterActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreate> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(AddEditChapterActivity.this);
            }
        });
    }

    private void deleteChapter(String chapterId){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/chapter/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ChapterClient client = retrofit.create(ChapterClient.class);
        Call<ResponseCreate> call = client.deleteChapter(chapterId);
        call.enqueue(new Callback<ResponseCreate>() {
            @Override
            public void onResponse(Call<ResponseCreate> call, Response<ResponseCreate> response) {
                dialogProgressBar.hideDialog();
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().getSuccess()){
                        Intent intent = new Intent(AddEditChapterActivity.this, ChaptersActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent,1);
                    }else{
                        new ErrorToast().showErrorMessage(AddEditChapterActivity.this);
                    }
                }else{
                    new ErrorToast().showErrorMessage(AddEditChapterActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreate> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(AddEditChapterActivity.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
