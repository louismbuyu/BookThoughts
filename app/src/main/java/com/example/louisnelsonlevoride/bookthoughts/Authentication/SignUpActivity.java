package com.example.louisnelsonlevoride.bookthoughts.Authentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louisnelsonlevoride.bookthoughts.DialogProgressBar;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreateUser;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.NavigationActivity;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.UserClient;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;
import com.example.louisnelsonlevoride.bookthoughts.ErrorToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText reenterPasswordEditText;
    private TextView firstLetterTextView;
    Button signUpBtn;
    private String TAG = "SignUpActivity";
    private byte[] byteArray;
    private StorageReference mStorageRef;
    private String username = "";
    private String password = "";
    private DialogProgressBar dialogProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle(getString(R.string.sign_up));
        mStorageRef = FirebaseStorage.getInstance().getReference();
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        reenterPasswordEditText = findViewById(R.id.reenter_password_edit_text);
        firstLetterTextView = findViewById(R.id.hidden_text_view);
        firstLetterTextView.setVisibility(View.INVISIBLE);
        dialogProgressBar = new DialogProgressBar(this);
        dialogProgressBar.setProgressTextView(getString(R.string.signing_up));

        signUpBtn = findViewById(R.id.sign_up_button);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpAction();
            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null){
                    if (!editable.toString().isEmpty()){
                        String firstLetter = ""+editable.toString().charAt(0);
                        firstLetterTextView.setText(firstLetter.toUpperCase());
                        convertViewToImage();
                    }
                }
            }
        });
    }

    private void convertViewToImage(){
        firstLetterTextView.setDrawingCacheEnabled(true);
        firstLetterTextView.buildDrawingCache(true);
        Bitmap bmp = Bitmap.createBitmap(firstLetterTextView.getDrawingCache());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();
    }

    private void uploadImage(final String username){
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
        String date = df.format(Calendar.getInstance().getTime());
        String childPath = username + "-"+ date +"-"+"image/profileImage.jpg";
        StorageReference storageReference = mStorageRef.child(childPath);
        storageReference.putBytes(byteArray)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        createUser(username,password,downloadUrl.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        dialogProgressBar.hideDialog();
                        new ErrorToast().showErrorMessage(SignUpActivity.this);
                    }
                });
    }

    public void signUpAction(){
        username = usernameEditText.getText().toString();
        Log.i(TAG,"First letter: "+username.charAt(0));
        password = passwordEditText.getText().toString();
        String reenterPassword = reenterPasswordEditText.getText().toString();

        if(!username.isEmpty() && !password.isEmpty() && (password.equals(reenterPassword))){
            dialogProgressBar.showDialog();
            uploadImage(username);
        }else{
            Toast.makeText(this,R.string.please_enter_username_password,Toast.LENGTH_LONG).show();
        }
    }

    private void createUser(String username, String password, String imageUrl){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/user/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<ResponseCreateUser> call = client.createUser(username,password,imageUrl);
        call.enqueue(new Callback<ResponseCreateUser>() {
            @Override
            public void onResponse(Call<ResponseCreateUser> call, Response<ResponseCreateUser> response) {
                dialogProgressBar.hideDialog();
                if (response.isSuccessful() && (response.body()!=null)){
                    ResponseCreateUser responseCreateUser = response.body();
                    if (responseCreateUser.getSuccess()){
                        User user = responseCreateUser.getUser();
                        CurrentUserSingleton.getInstance().setUserId(SignUpActivity.this,user.getUserId());
                        CurrentUserSingleton.getInstance().setUsername(SignUpActivity.this,user.getUsername());
                        Intent intent = new Intent(SignUpActivity.this,NavigationActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(SignUpActivity.this, R.string.oops_please_enter_username,Toast.LENGTH_LONG).show();
                    }
                }else{
                    new ErrorToast().showErrorMessage(SignUpActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreateUser> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(SignUpActivity.this);
            }
        });
    }
}
