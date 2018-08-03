package com.example.louisnelsonlevoride.bookthoughts.Books.Quotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.Models.Chapter;
import com.example.louisnelsonlevoride.bookthoughts.Services.DownloadImageTask;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;
import com.example.louisnelsonlevoride.bookthoughts.Models.Quote;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Users.UsersActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class DisplayQuoteActivity extends AppCompatActivity {

    ImageView displayQuoteImageView;
    TextView bookTitleTextView;
    TextView thoughtTextView;
    //private View mView;
    private RelativeLayout shareLayout;
    Quote quote;
    Book book;
    Chapter chapter;
    Button shareBtn;
    Button editBtn;
    String username;
    private String TAG = "DisplayQuoteActivity";
    private StorageReference mStorageRef;
    private byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_quote);
        setTitle(getString(R.string.display_quote));
        mStorageRef = FirebaseStorage.getInstance().getReference();
        username = CurrentUserSingleton.getInstance().getUsername(this);
        displayQuoteImageView = findViewById(R.id.display_quote_image);
        bookTitleTextView = findViewById(R.id.display_quote_book_title);
        thoughtTextView = findViewById(R.id.new_quote_thought_text_view);
        //mView = findViewById(R.id.shareable_layout);
        shareLayout = findViewById(R.id.shareable_layout);
        shareBtn = findViewById(R.id.quote_share_button);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //convertImage();
                convertViewToImage();
            }
        });
        editBtn = findViewById(R.id.quote_edit_button);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToEdit();
            }
        });

        Quote mQuote = getIntent().getParcelableExtra("quote");
        if (mQuote!=null){
            quote = mQuote;
            //RequestOptions options = new RequestOptions()
            //        .centerCrop();
            //Glide.with(this).load(quote.getImageUrl()).apply(options).into(displayQuoteImageView);
            new DownloadImageTask(displayQuoteImageView).execute(quote.getImageUrl());

            Book mBook = getIntent().getParcelableExtra("book");
            if (mBook!=null){
                book = mBook;
            }

            Chapter mChapter = getIntent().getParcelableExtra("chapter");
            if (mChapter!=null){
                chapter = mChapter;
            }

            bookTitleTextView.setText(book.getTitle());
            thoughtTextView.setText(quote.getThought());
        }
    }

    private void convertViewToImage(){
        shareLayout.setDrawingCacheEnabled(true);
        shareLayout.buildDrawingCache(false);
        Bitmap tempBmp = Bitmap.createBitmap(shareLayout.getDrawingCache());
        Bitmap bmp = getResizedBitmap(tempBmp,900);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();
        selectUser(byteArray);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void selectUser(byte[] bytes){
        Intent intent = new Intent(DisplayQuoteActivity.this, UsersActivity.class);
        intent.putExtra("image",bytes);
        startActivity(intent);
    }

    private void transitionToEdit(){
        Intent intent = new Intent(this,AddEditQuoteActivity.class);
        intent.putExtra("quote",quote);
        intent.putExtra("update", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
    }
}
