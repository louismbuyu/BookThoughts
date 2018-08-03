package com.example.louisnelsonlevoride.bookthoughts.Profile;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.louisnelsonlevoride.bookthoughts.DialogProgressBar;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreate;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.UserClient;
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

public class ProfileActivity extends AppCompatActivity implements SelectProfileItemInteface {

    RecyclerView recyclerView;
    ProfileViewAdapter profileViewAdapter;
    User user;
    Boolean edit = false;
    String TAG = "ProfileActivity";
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String displayName;
    Boolean imageChanged = false;
    Boolean displayNameChanged = false;
    private StorageReference mStorageRef;
    private DialogProgressBar dialogProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        recyclerView = findViewById(R.id.profile_view_recycle_view);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        User tempUser = getIntent().getParcelableExtra("user");
        dialogProgressBar = new DialogProgressBar(this);
        dialogProgressBar.setProgressTextView(getString(R.string.please_wait));
        if (tempUser != null){
            user = tempUser;
            imageUri = Uri.parse(user.getImageUrl());
            displayName = user.getDisplayName();
            setTitle(user.getUsername());
            setupViews();
        }
    }

    private void setupViews(){
        profileViewAdapter = new ProfileViewAdapter(this,user,this,edit,imageUri,displayName);
        recyclerView.setAdapter(profileViewAdapter);
    }

    @Override
    public void selectItem(int position) {
        if (position == 0){
            getImageFromPhone();
        }else if (position == 1){
        }else if (position == 2){
            transitionToEdit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_btn:
                if (edit){
                    item.setTitle(R.string.edit);
                    edit = false;
                    validateChanges();
                    setupViews();
                }else {
                    item.setTitle(R.string.save);
                    edit = true;
                    setupViews();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getImageFromPhone(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    private void transitionToEdit(){
        Intent intent = new Intent(this,EditProfileActivity.class);
        intent.putExtra("displayName",displayName);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageChanged = true;
            setupViews();
        }

        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String tempDisplayName = data.getStringExtra("displayName");
                if(tempDisplayName!=null){
                    displayName = tempDisplayName;
                    displayNameChanged = true;
                    setupViews();
                }
            }
        }
    }

    private void validateChanges(){
        if (imageChanged){
            dialogProgressBar.showDialog();
            uploadImage();
        }else {
            if (displayNameChanged){
                dialogProgressBar.showDialog();
                updateProfile("",displayName);
            }
        }
    }

    private void uploadImage(){
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
        String date = df.format(Calendar.getInstance().getTime());
        String childPath = user.getUsername() + "-"+ date +"-"+"image/profile.jpg";
        StorageReference riversRef = mStorageRef.child(childPath);
        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        if (displayNameChanged){
                            updateProfile(downloadUrl.toString(),displayName);
                        }else {
                            updateProfile(downloadUrl.toString(),"");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        dialogProgressBar.hideDialog();
                        new ErrorToast().showErrorMessage(ProfileActivity.this);
                    }
                });
    }

    private void updateProfile(String imageUrl, String newDisplayName){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/user/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<ResponseCreate> call = client.updateUser(newDisplayName,imageUrl,user.getUsername());
        call.enqueue(new Callback<ResponseCreate>() {
            @Override
            public void onResponse(Call<ResponseCreate> call, Response<ResponseCreate> response) {
                dialogProgressBar.hideDialog();
                if(response.body().getSuccess()){
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    new ErrorToast().showErrorMessage(ProfileActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreate> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(ProfileActivity.this);
            }
        });
    }
}
