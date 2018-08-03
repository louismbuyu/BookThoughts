package com.example.louisnelsonlevoride.bookthoughts.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.louisnelsonlevoride.bookthoughts.DBData.AppExecutors;
import com.example.louisnelsonlevoride.bookthoughts.DBData.BookDatabase;
import com.example.louisnelsonlevoride.bookthoughts.DialogProgressBar;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.Onboarding.MainActivity;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;

public class SettingsActivity extends AppCompatActivity {

    Button logoutBtn;
    User user;
    BookDatabase bookDatabase;
    private DialogProgressBar dialogProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.settings));
        bookDatabase = BookDatabase.getInstance(getApplicationContext());
        dialogProgressBar = new DialogProgressBar(this);
        dialogProgressBar.setProgressTextView(getString(R.string.please_wait));

        User tempUser = getIntent().getParcelableExtra("user");
        if (tempUser != null){
            user = tempUser;
        }

        logoutBtn = findViewById(R.id.settings_logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutAction();
            }
        });
    }

    private void logoutAction(){
        dialogProgressBar.showDialog();
        CurrentUserSingleton.getInstance().setUsername(this,null);
        CurrentUserSingleton.getInstance().setUserId(this,null);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                bookDatabase.BookDao().deleteAllBooks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogProgressBar.hideDialog();
                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
