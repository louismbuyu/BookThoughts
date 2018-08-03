package com.example.louisnelsonlevoride.bookthoughts;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class ErrorToast {

    public ErrorToast() {
    }

    public void showErrorMessage(Context context){
        Toast.makeText(context, R.string.something_went_wrong,Toast.LENGTH_LONG).show();
    }
}
