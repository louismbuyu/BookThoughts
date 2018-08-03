package com.example.louisnelsonlevoride.bookthoughts.Books.CustomBook;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.R;

public class DialogChoosePhoto {

    public ImageView cameraImageView;
    public TextView cameraTextView;

    public ImageView galleryImageView;
    public TextView galleryTextView;

    public Button cancelButton;
    public Dialog dialog;
    public void showDialog(Activity activity){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_choose_photo);

        cameraImageView = dialog.findViewById(R.id.camera_choice_image_view);
        cameraTextView = dialog.findViewById(R.id.camera_choice_text_view);

        galleryImageView = dialog.findViewById(R.id.gallery_choice_image_view);
        galleryTextView = dialog.findViewById(R.id.gallery_choice_text_view);

        cancelButton = dialog.findViewById(R.id.choose_cancel_btn);
        dialog.show();
    }
}
