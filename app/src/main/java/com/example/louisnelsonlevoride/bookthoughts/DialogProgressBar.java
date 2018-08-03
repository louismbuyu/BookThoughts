package com.example.louisnelsonlevoride.bookthoughts;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DialogProgressBar {

    private ProgressBar progressBar;
    private TextView progressTextView;
    private Dialog dialog;

    public DialogProgressBar(Activity activity) {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_progress_bar);

        progressBar = dialog.findViewById(R.id.dialog_progress_bar);
        progressTextView = dialog.findViewById(R.id.dialog_progress_text_view);
    }

    public void showDialog(){
        dialog.show();
    }

    public void setProgressTextView(String message) {
        this.progressTextView.setText(message);
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}
