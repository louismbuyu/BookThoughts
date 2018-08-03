package com.example.louisnelsonlevoride.bookthoughts.Books;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.louisnelsonlevoride.bookthoughts.Books.iTunes.ItunesActivity;
import com.example.louisnelsonlevoride.bookthoughts.R;

public class AddBookInfoActivity extends AppCompatActivity {

    private EditText bookTitleEditText;
    private EditText bookAuthorEditText;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_info);

        bookTitleEditText = findViewById(R.id.book_title_edit_text);
        bookAuthorEditText = findViewById(R.id.book_author_edit_text);
        nextButton = findViewById(R.id.add_book_title_next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
    }

    private void nextActivity(){
        String title = bookTitleEditText.getText().toString();
        String author = bookAuthorEditText.getText().toString();
        if (!title.isEmpty() && !author.isEmpty()){
            Intent intent = new Intent(this, ItunesActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("author",author);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
        }
    }
}
