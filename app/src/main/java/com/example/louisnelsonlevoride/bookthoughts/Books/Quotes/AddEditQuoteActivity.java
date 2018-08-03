package com.example.louisnelsonlevoride.bookthoughts.Books.Quotes;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.DialogProgressBar;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.Models.Chapter;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;
import com.example.louisnelsonlevoride.bookthoughts.Models.Quote;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreate;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.QuoteClient;
import com.example.louisnelsonlevoride.bookthoughts.ErrorToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEditQuoteActivity extends AppCompatActivity {

    ImageView quoteImage;
    Book book;
    Chapter chapter;
    Quote quote;
    EditText thoughtEditText;
    Button saveButton;
    Button deleteButton;
    Boolean update;
    String username;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    private StorageReference mStorageRef;
    private String TAG = "AddEditQuoteActivity";
    private DialogProgressBar dialogProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_quote);
        username = CurrentUserSingleton.getInstance().getUsername(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        setTitle(getString(R.string.quote));

        quoteImage = findViewById(R.id.new_quote_image);
        thoughtEditText = findViewById(R.id.new_quote_thought_edit_text);
        saveButton = findViewById(R.id.new_quote_save_button);
        deleteButton = findViewById(R.id.new_quote_delete_button);
        dialogProgressBar = new DialogProgressBar(this);
        dialogProgressBar.setProgressTextView(getString(R.string.please_wait));

        Quote tempQuote = getIntent().getParcelableExtra("quote");
        if(tempQuote!=null){
            quote = tempQuote;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.defaultbook)
                    .error(R.drawable.defaultbook);
            Glide.with(this).load(quote.getImageUrl()).apply(options).into(quoteImage);
            thoughtEditText.setText(quote.getThought());
        }else{
            Book tempBook = getIntent().getParcelableExtra("book");
            if(tempBook!=null){
                book = tempBook;
            }

            Chapter tempChapter = getIntent().getParcelableExtra("chapter");
            if(tempChapter!=null){
                chapter = tempChapter;
            }
        }

        update = getIntent().getBooleanExtra("update",false);
        if (update){
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteQuote(quote.get_id());
                }
            });
        }else{
            deleteButton.setVisibility(View.INVISIBLE);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!thoughtEditText.getText().toString().isEmpty()) {
                    if (update){
                        if (imageUri!=null){
                            //Update with image
                            uploadImageToFirebase(imageUri);
                        }else{
                            //Update with no image
                            updateQuote(quote.get_id(),quote.getImageUrl(),thoughtEditText.getText().toString());
                        }
                    }else{
                        if (imageUri!=null){
                            //Completely new quote
                            uploadImageToFirebase(imageUri);
                        }else{
                            Toast.makeText(AddEditQuoteActivity.this, R.string.please_select_an_image, Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    Toast.makeText(AddEditQuoteActivity.this, R.string.please_enter_a_thought_text, Toast.LENGTH_LONG).show();
                }
            }
        });

        quoteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void selectImage(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            quoteImage.setImageURI(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri){
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
        String date = df.format(Calendar.getInstance().getTime());
        String childPath = username + "-"+ date +"-"+"image/quote.jpg";
        StorageReference storageReference = mStorageRef.child(childPath);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        if(update){
                            updateQuote(quote.get_id(),downloadUrl.toString(),thoughtEditText.getText().toString());
                        }else{
                            saveQuote(username,book.getId(),chapter.get_id(),downloadUrl.toString(),thoughtEditText.getText().toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        dialogProgressBar.hideDialog();
                        new ErrorToast().showErrorMessage(AddEditQuoteActivity.this);
                    }
                });
    }

    private void saveQuote(String username, String bookId, String chapterId, String imageUrl, String thought){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/quote/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        QuoteClient client = retrofit.create(QuoteClient.class);
        Call<ResponseCreate> call = client.createQuote(username,
                bookId,
                chapterId, imageUrl,thought);
        call.enqueue(new Callback<ResponseCreate>() {
            @Override
            public void onResponse(Call<ResponseCreate> call, Response<ResponseCreate> response) {
                dialogProgressBar.hideDialog();
                if(response.body().getSuccess()){
                    Intent intent = new Intent(AddEditQuoteActivity.this, QuotesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent,1);
                }else{
                    new ErrorToast().showErrorMessage(AddEditQuoteActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreate> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(AddEditQuoteActivity.this);
            }
        });
    }

    private void updateQuote(String quoteId, String imageUrl, String thought){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/quote/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        QuoteClient client = retrofit.create(QuoteClient.class);
        Call<ResponseCreate> call = client.updateQuote(quoteId, imageUrl, thought);
        call.enqueue(new Callback<ResponseCreate>() {
            @Override
            public void onResponse(Call<ResponseCreate> call, Response<ResponseCreate> response) {
                dialogProgressBar.hideDialog();
                if(response.body().getSuccess()){
                    Intent intent = new Intent(AddEditQuoteActivity.this, QuotesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent,1);
                }else{
                    new ErrorToast().showErrorMessage(AddEditQuoteActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreate> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(AddEditQuoteActivity.this);
            }
        });
    }

    private void deleteQuote(String quoteId){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/quote/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        QuoteClient client = retrofit.create(QuoteClient.class);
        Call<ResponseCreate> call = client.deleteQuote(quoteId);
        call.enqueue(new Callback<ResponseCreate>() {
            @Override
            public void onResponse(Call<ResponseCreate> call, Response<ResponseCreate> response) {
                dialogProgressBar.hideDialog();
                if(response.body().getSuccess()){
                    Intent intent = new Intent(AddEditQuoteActivity.this, QuotesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent,1);
                }else{
                    new ErrorToast().showErrorMessage(AddEditQuoteActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreate> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(AddEditQuoteActivity.this);
            }
        });
    }
}
