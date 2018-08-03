package com.example.louisnelsonlevoride.bookthoughts.Users;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.louisnelsonlevoride.bookthoughts.DialogProgressBar;
import com.example.louisnelsonlevoride.bookthoughts.DialogSuccess;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseMessage;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseUsers;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.MessageClient;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersActivity extends AppCompatActivity implements SelectUserInterface {

    RecyclerView recyclerView;
    UsersAdapter usersAdapter;
    DialogSuccess dialogSuccess;
    List<User> users;
    byte[] displayThought;
    private Menu mOptionsMenu;
    private String TAG = "UsersActivity";
    private StorageReference mStorageRef;
    private String username;
    private String senderId;
    private String receiverId;
    private DialogProgressBar dialogProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_users);
        setTitle(getString(R.string.search_users));
        recyclerView = findViewById(R.id.users_recycle_view);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        username = CurrentUserSingleton.getInstance().getUsername(this);
        senderId = CurrentUserSingleton.getInstance().getUserId(this);
        dialogProgressBar = new DialogProgressBar(this);
        dialogProgressBar.setProgressTextView(getString(R.string.please_wait));

        byte[] tempBytes = getIntent().getByteArrayExtra("image");
        if(tempBytes!=null){
            displayThought = tempBytes;
        }
    }

    private void setupViews(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        usersAdapter = new UsersAdapter(users,this,this);
        recyclerView.setAdapter(usersAdapter);
    }

    private void getUsers(String username){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/user/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<ResponseUsers> call = client.searchUser(username);
        call.enqueue(new Callback<ResponseUsers>() {
            @Override
            public void onResponse(Call<ResponseUsers> call, Response<ResponseUsers> response) {
                dialogProgressBar.hideDialog();
                if (response.isSuccessful() && (response.body()!=null)){
                    ResponseUsers responseUsers = response.body();
                    if (responseUsers.getSuccess()){
                        users = responseUsers.getUsers();
                        setupViews();
                    }else{
                        new ErrorToast().showErrorMessage(UsersActivity.this);
                    }
                }else{
                    new ErrorToast().showErrorMessage(UsersActivity.this);
                }
            }
            @Override
            public void onFailure(Call<ResponseUsers> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(UsersActivity.this);
            }
        });
    }

    private void setupSearch(){
        MenuItem ourSearchItem = mOptionsMenu.findItem(R.id.action_search);
        SearchView sv = (SearchView) ourSearchItem.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getUsers(newText);
                return true;
            }
        });
    }

    @Override
    public void selectUser(final int position) {
        String shareWith = getString(R.string.share_thought_with);
        new AlertDialog.Builder(this)
                .setTitle(R.string.share_thought)
                .setMessage(shareWith+" "+users.get(position).getUsername()+" ?")
                .setIcon(R.drawable.thought)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        validateMessage(users.get(position));
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        setupSearch();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void validateMessage(User user){
        if (user.getUsername() != username){
            dialogProgressBar.showDialog();
            receiverId = user.getUserId();
            saveToFirebase();
        }else{
            dialogProgressBar.hideDialog();
            Toast.makeText(UsersActivity.this, R.string.sorry_cant_share_with_self, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToFirebase(){
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
        String date = df.format(Calendar.getInstance().getTime());
        String childPath = username + "-"+ date +"-";
        StorageReference storageReference = mStorageRef.child(childPath+"image/thought.jpg");
        storageReference.putBytes(displayThought)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        saveMessage(senderId,receiverId,downloadUrl.toString(),"");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        dialogProgressBar.hideDialog();
                        new ErrorToast().showErrorMessage(UsersActivity.this);
                    }
                });
    }

    private void saveMessage(String senderId, String receiverId,String imageUrl, String text){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/message/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        MessageClient client = retrofit.create(MessageClient.class);
        Call<ResponseMessage> call = client.createMessage(senderId,receiverId,imageUrl,text);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                dialogProgressBar.hideDialog();
                if (response.isSuccessful() && (response.body()!=null)){
                    ResponseMessage responseCreate = response.body();
                    if (responseCreate.getSuccess()){
                        successDialog();
                    }else{
                        new ErrorToast().showErrorMessage(UsersActivity.this);
                    }
                }else{
                    new ErrorToast().showErrorMessage(UsersActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(UsersActivity.this);
            }
        });
    }

    private void successDialog(){
        dialogSuccess = new DialogSuccess(this);
        dialogSuccess.showDialog(true);
        dialogSuccess.successBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSuccess.dialog.dismiss();
                finish();
            }
        });
    }
}
