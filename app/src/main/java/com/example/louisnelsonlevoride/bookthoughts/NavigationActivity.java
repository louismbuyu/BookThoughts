package com.example.louisnelsonlevoride.bookthoughts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.Books.AddBookInfoActivity;
import com.example.louisnelsonlevoride.bookthoughts.Books.BooksFragment;
import com.example.louisnelsonlevoride.bookthoughts.Chat.ChatFragment;
import com.example.louisnelsonlevoride.bookthoughts.Onboarding.MainActivity;
import com.example.louisnelsonlevoride.bookthoughts.Profile.ProfileFragment;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;
import com.example.louisnelsonlevoride.bookthoughts.Users.UsersActivity;

public class NavigationActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String TAG = "NavigationActivity";
    private int showAddButton = 1;
    private Menu mMenu;
    private String userId;
    private ChatFragment chatFragment;
    private BooksFragment booksFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_books:
                    setTitle(R.string.books);
                    showAddButton = 1;
                    mMenu.findItem(R.id.add_new_book_item).setVisible(true);
                    BooksFragment fragment = new BooksFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame,fragment,"FragmentName");
                    booksFragment = fragment;
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_chat:
                    setTitle(R.string.chats);
                    mMenu.findItem(R.id.add_new_book_item).setVisible(false);
                    mMenu.findItem(R.id.add_new_book_item).setEnabled(false);
                    ChatFragment fragment2 = new ChatFragment();
                    showAddButton = 2;
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.frame,fragment2,"FragmentName2");
                    chatFragment = fragment2;
                    fragmentTransaction2.commit();
                    return true;
                case R.id.navigation_profile:
                    setTitle(R.string.profile);
                    mMenu.findItem(R.id.add_new_book_item).setVisible(false);
                    mMenu.findItem(R.id.add_new_book_item).setEnabled(false);
                    ProfileFragment fragment3 = new ProfileFragment();
                    showAddButton = 0;
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.frame,fragment3,"FragmentName3");
                    fragmentTransaction3.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        userId = CurrentUserSingleton.getInstance().getUserId(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setTitle(R.string.books);
        BooksFragment fragment = new BooksFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment,"FragmentName");
        booksFragment = fragment;
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_book_item:
                if (showAddButton == 1){
                    Intent intent = new Intent(NavigationActivity.this, AddBookInfoActivity.class);
                    startActivityForResult(intent,1);
                }else if (showAddButton == 2){
                    Intent intent = new Intent(NavigationActivity.this, UsersActivity.class);
                    startActivity(intent);
                }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(booksFragment);
            ft.attach(booksFragment);
            ft.commit();
        }else if (requestCode == 2){
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(chatFragment);
            ft.attach(chatFragment);
            ft.commit();
        }
    }
}
