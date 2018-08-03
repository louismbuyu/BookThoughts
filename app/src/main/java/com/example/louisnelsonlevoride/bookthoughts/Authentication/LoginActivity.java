package com.example.louisnelsonlevoride.bookthoughts.Authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.louisnelsonlevoride.bookthoughts.DialogProgressBar;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreateUser;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.NavigationActivity;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.UserClient;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;
import com.example.louisnelsonlevoride.bookthoughts.ErrorToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private String TAG = "LoginActivity";
    private DialogProgressBar dialogProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getString(R.string.log_in));
        loginBtn = findViewById(R.id.login_button);
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.login_password_edit_text);
        dialogProgressBar = new DialogProgressBar(this);
        dialogProgressBar.setProgressTextView(getString(R.string.logging_in));

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInAction();
            }
        });
    }

    public void signInAction(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(!username.isEmpty() && !password.isEmpty()){
            dialogProgressBar.showDialog();
            signUserIn(username,password);
        }else {
            Toast.makeText(this, R.string.please_enter_username_password,Toast.LENGTH_LONG).show();
        }
    }

    private void signUserIn(String username, String password){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/user/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<ResponseCreateUser> call = client.signin(username,password);
        call.enqueue(new Callback<ResponseCreateUser>() {
            @Override
            public void onResponse(Call<ResponseCreateUser> call, Response<ResponseCreateUser> response) {
                dialogProgressBar.hideDialog();
                if (response.isSuccessful() && (response.body()!=null)){
                    ResponseCreateUser responseCreateUser = response.body();
                    if (responseCreateUser.getSuccess()){
                        User user = responseCreateUser.getUser();
                        CurrentUserSingleton.getInstance().setUserId(LoginActivity.this,user.getUserId());
                        CurrentUserSingleton.getInstance().setUsername(LoginActivity.this,user.getUsername());
                        Intent intent = new Intent(LoginActivity.this,NavigationActivity.class);
                        startActivity(intent);
                    }else{
                        new ErrorToast().showErrorMessage(LoginActivity.this);
                    }
                }else{
                    new ErrorToast().showErrorMessage(LoginActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreateUser> call, Throwable t) {
                dialogProgressBar.hideDialog();
                new ErrorToast().showErrorMessage(LoginActivity.this);
            }
        });
    }
}
