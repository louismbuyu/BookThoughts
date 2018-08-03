package com.example.louisnelsonlevoride.bookthoughts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogSuccess {

    public ImageView successImage;
    public TextView sucessTitle;
    public TextView successDescription;
    public Button successBtn;
    public Dialog dialog;

    public DialogSuccess(Activity activity) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_success);

        successImage = dialog.findViewById(R.id.success_image);
        sucessTitle = dialog.findViewById(R.id.success_title);
        successDescription = dialog.findViewById(R.id.success_description);
        successBtn = dialog.findViewById(R.id.success_button);
    }

    @SuppressLint("ResourceAsColor")
    public void showDialog(Boolean success){
        if (!success){
            successImage.setImageResource(R.drawable.error);
            sucessTitle.setText(R.string.oops);
            successDescription.setText(R.string.something_went_wrong);
            successBtn.setTextColor(R.color.colorAccent);
        }

        dialog.show();
    }
}
