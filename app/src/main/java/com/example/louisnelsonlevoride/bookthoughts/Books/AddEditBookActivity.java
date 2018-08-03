package com.example.louisnelsonlevoride.bookthoughts.Books;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Books.CustomBook.DialogChoosePhoto;
import com.example.louisnelsonlevoride.bookthoughts.DBData.AppExecutors;
import com.example.louisnelsonlevoride.bookthoughts.DBData.BookDatabase;
import com.example.louisnelsonlevoride.bookthoughts.DialogProgressBar;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreateBook;
import com.example.louisnelsonlevoride.bookthoughts.NavigationActivity;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.BookClient;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;
import com.example.louisnelsonlevoride.bookthoughts.ErrorToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEditBookActivity extends AppCompatActivity {

    private String TAG = "AddEditBookActivity";

    private ImageView bookImage;
    private EditText titleEditText;
    private EditText authorEditText;
    private Button saveButton;
    private Button deleteButton;

    private String title;
    private String author;
    private String username;
    private Boolean newBook;
    private Book book;
    private DialogChoosePhoto alert;
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private Bitmap bitmap;
    private byte[] byteArray;
    private String mediaPath;
    private StorageReference mStorageRef;
    private BookDatabase bookDatabase;
    private DialogProgressBar dialogProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);
        username = CurrentUserSingleton.getInstance().getUsername(this);
        bookDatabase = BookDatabase.getInstance(getApplicationContext());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        bookImage = findViewById(R.id.edit_book_image_view);
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhotoFromGallery();
            }
        });

        titleEditText = findViewById(R.id.edit_book_title_edit_text);
        authorEditText = findViewById(R.id.edit_book_author_edit_text);
        newBook = getIntent().getBooleanExtra("type",true);
        deleteButton = findViewById(R.id.edit_book_delete_button);
        saveButton = findViewById(R.id.edit_book_save_button);
        dialogProgressBar = new DialogProgressBar(this);
        dialogProgressBar.setProgressTextView(getString(R.string.please_wait));

        if (newBook){
            setTitle(getString(R.string.new_book));
            title = getIntent().getStringExtra("title");
            titleEditText.setText(title);
            author = getIntent().getStringExtra("author");
            authorEditText.setText(author);
            deleteButton.setVisibility(View.INVISIBLE);
            saveButton.setEnabled(false);
            saveButton.setAlpha(0.5f);
        }else{
            book = getIntent().getParcelableExtra("book");
            titleEditText.setText(book.getTitle());
            setTitle(getString(R.string.update_delete_book));
            authorEditText.setText(book.getAuthor());
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.defaultbook)
                    .error(R.drawable.defaultbook);
            Glide.with(this).load(book.getImageUrl()).apply(options).into(bookImage);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteBook(book.getId());
                }
            });
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newBook){
                    if (!titleEditText.getText().toString().isEmpty() && !authorEditText.getText().toString().isEmpty()){
                        if (!titleEditText.getText().toString().isEmpty() && !authorEditText.getText().toString().isEmpty()){
                            title = titleEditText.getText().toString();
                            author = authorEditText.getText().toString();
                            firebaseUpload(imageUri);
                        }
                    }
                }else{
                    if (!titleEditText.getText().toString().isEmpty() && !authorEditText.getText().toString().isEmpty()){
                        if (!titleEditText.getText().toString().isEmpty() && !authorEditText.getText().toString().isEmpty()){
                            title = titleEditText.getText().toString();
                            author = authorEditText.getText().toString();

                            if (imageUri == null){
                                updateBook(book.getId(),"");
                            }else{
                                firebaseUpload(imageUri);
                            }
                        }
                    }

                }
            }
        });
    }

    /*private void selectImage(){
        alert = new DialogChoosePhoto();
        alert.showDialog(this);
        alert.cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhotoFromCamera();
            }
        });
        alert.cameraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhotoFromCamera();
            }
        });
        alert.galleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhotoFromGallery();
            }
        });
        alert.galleryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhotoFromGallery();
            }
        });
        alert.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dialog.dismiss();
            }
        });
    }*/

    private void selectPhotoFromCamera(){

    }

    private void selectPhotoFromGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    public void saveBook(String imageUrl){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/book/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        BookClient client = retrofit.create(BookClient.class);
        Call<ResponseCreateBook> call = client.createBookWithImageUrlFirebase(username,
                title,
                author, imageUrl);
        call.enqueue(new Callback<ResponseCreateBook>() {
            @Override
            public void onResponse(Call<ResponseCreateBook> call, Response<ResponseCreateBook> response) {
                if (response.body() != null){
                    dialogProgressBar.hideDialog();
                    transitionBack();
                }else {
                    dialogProgressBar.hideDialog();
                    new ErrorToast().showErrorMessage(AddEditBookActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreateBook> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(AddEditBookActivity.this);
            }
        });
    }

    private void firebaseUpload(Uri fileUri){
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
        String date = df.format(Calendar.getInstance().getTime());
        String childPath = username + "-"+ date +"-"+"image/book.jpg";
        StorageReference storageReference = mStorageRef.child(childPath);
        storageReference.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        if (newBook){
                            saveBook(downloadUrl.toString());
                        }else{
                            updateBook(book.getId(),downloadUrl.toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        dialogProgressBar.hideDialog();
                        new ErrorToast().showErrorMessage(AddEditBookActivity.this);
                    }
                });
    }

    private void updateBook(String bookId, String imageUrl){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/book/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        BookClient client = retrofit.create(BookClient.class);
        Call<ResponseCreateBook> call = client.updateBook(bookId,
                title,
                author, imageUrl);
        call.enqueue(new Callback<ResponseCreateBook>() {
            @Override
            public void onResponse(Call<ResponseCreateBook> call, Response<ResponseCreateBook> response) {
                if (response.body() != null){
                    if (response.body() != null){
                        deleteOldBook(response.body().getBook());
                    }else{
                        dialogProgressBar.hideDialog();
                        new ErrorToast().showErrorMessage(AddEditBookActivity.this);
                    }
                }else {
                    dialogProgressBar.hideDialog();
                    new ErrorToast().showErrorMessage(AddEditBookActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreateBook> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(AddEditBookActivity.this);
            }
        });
    }

    private void updateBookRoom(final Book updatedBook){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                bookDatabase.BookDao().insertBook(updatedBook);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogProgressBar.hideDialog();
                        transitionBack();
                    }
                });
            }
        });
    }

    private void deleteOldBook(final Book updatedBook){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                bookDatabase.BookDao().deleteBook(book);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateBookRoom(updatedBook);
                    }
                });
            }
        });
    }

    private void deleteBook(String bookId){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/book/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        BookClient client = retrofit.create(BookClient.class);
        Call<ResponseCreateBook> call = client.deleteBook(bookId);
        call.enqueue(new Callback<ResponseCreateBook>() {
            @Override
            public void onResponse(Call<ResponseCreateBook> call, Response<ResponseCreateBook> response) {
                if (response.body() != null){
                    if (response.body() != null){
                        if (book.getId().equals(CurrentUserSingleton.getInstance().getFavouriteBook(AddEditBookActivity.this))){
                            CurrentUserSingleton.getInstance().setFavouriteBookTitle(AddEditBookActivity.this,null);
                            CurrentUserSingleton.getInstance().setFavouriteBook(AddEditBookActivity.this,null);
                            CurrentUserSingleton.getInstance().setFavouriteBookImageUrl(AddEditBookActivity.this,null);
                        }
                        deleteBookRoom();
                    }else{
                        dialogProgressBar.hideDialog();
                        new ErrorToast().showErrorMessage(AddEditBookActivity.this);
                    }
                }else {
                    dialogProgressBar.hideDialog();
                    new ErrorToast().showErrorMessage(AddEditBookActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreateBook> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(AddEditBookActivity.this);
            }
        });
    }

    private void deleteBookRoom(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                bookDatabase.BookDao().deleteBook(book);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogProgressBar.hideDialog();
                        transitionBack();
                    }
                });
            }
        });
    }

    private void transitionBack(){
        Intent intent = new Intent(AddEditBookActivity.this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byteArray = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bookImage.setImageBitmap(bitmap);
            alert.dialog.dismiss();
            saveButton.setEnabled(true);
            saveButton.setAlpha(1f);
        }
    }

}
