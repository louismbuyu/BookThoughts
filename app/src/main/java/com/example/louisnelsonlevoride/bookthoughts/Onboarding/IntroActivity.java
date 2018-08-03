package com.example.louisnelsonlevoride.bookthoughts.Onboarding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.louisnelsonlevoride.bookthoughts.Authentication.LoginActivity;
import com.example.louisnelsonlevoride.bookthoughts.Authentication.SignUpActivity;
import com.example.louisnelsonlevoride.bookthoughts.R;

public class IntroActivity extends AppCompatActivity {

    private Button loginBtn;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        loginBtn = findViewById(R.id.loginBtnId);
        signUpBtn = findViewById(R.id.signupBtnId);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    private void login(){
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
    }

    private void signup(){
        Intent intent = new Intent(this, SignUpActivity.class);
        this.startActivity(intent);
    }
}
