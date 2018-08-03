package com.example.louisnelsonlevoride.bookthoughts.Singletons;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.louisnelsonlevoride.bookthoughts.Onboarding.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class CurrentUserSingleton {
    private static CurrentUserSingleton INSTANCE = new CurrentUserSingleton();

    // other instance variables can be here

    private CurrentUserSingleton() {}

    public static CurrentUserSingleton getInstance() {
        return(INSTANCE);
    }

    public String getUserId(Context context){
        SharedPreferences prefs = context.getSharedPreferences("bookthoughts", MODE_PRIVATE);
        String userId = prefs.getString("USERID", null);
        return userId;
    }

    public String getUsername(Context context){
        SharedPreferences prefs = context.getSharedPreferences("bookthoughts", MODE_PRIVATE);
        String username = prefs.getString("USERNAME", null);
        return username;
    }

    public void setUserId(Context context, String userId){
        SharedPreferences.Editor editor = context.getSharedPreferences("bookthoughts", MODE_PRIVATE).edit();
        editor.putString("USERID", userId);
        editor.commit();
    }

    public void setUsername(Context context, String username){
        SharedPreferences.Editor editor = context.getSharedPreferences("bookthoughts", MODE_PRIVATE).edit();
        editor.putString("USERNAME", username);
        editor.commit();
    }

    public void setFavouriteBook(Context context, String bookId){
        SharedPreferences.Editor editor = context.getSharedPreferences("bookthoughts",MODE_PRIVATE).edit();
        editor.putString("FAVOURITE_BOOK",bookId);
        editor.commit();
    }

    public String getFavouriteBook(Context context){
        SharedPreferences prefs = context.getSharedPreferences("bookthoughts", MODE_PRIVATE);
        String bookId = prefs.getString("FAVOURITE_BOOK", null);
        return bookId;
    }

    public void setFavouriteBookTitle(Context context, String bookTitle){
        SharedPreferences.Editor editor = context.getSharedPreferences("bookthoughts",MODE_PRIVATE).edit();
        editor.putString("FAVOURITE_BOOK_TITLE",bookTitle);
        editor.commit();
    }

    public String getFavouriteBookTitle(Context context){
        SharedPreferences prefs = context.getSharedPreferences("bookthoughts", MODE_PRIVATE);
        String bookTitle = prefs.getString("FAVOURITE_BOOK_TITLE", null);
        return bookTitle;
    }

    public void setFavouriteBookImageUrl(Context context, String bookImageUrl){
        SharedPreferences.Editor editor = context.getSharedPreferences("bookthoughts",MODE_PRIVATE).edit();
        editor.putString("FAVOURITE_BOOK_IMAGE_URL",bookImageUrl);
        editor.commit();

    }

    public String getFavouriteBookImageUrl(Context context){
        SharedPreferences prefs = context.getSharedPreferences("bookthoughts", MODE_PRIVATE);
        String bookImageUrl = prefs.getString("FAVOURITE_BOOK_IMAGE_URL", null);
        return bookImageUrl;
    }
}
