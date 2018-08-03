package com.example.louisnelsonlevoride.bookthoughts.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.R;

public class CurrentUserImageVH extends RecyclerView.ViewHolder {

    ImageView profileImageView;
    ImageView messageImageView;
    TextView timeStampTextView;

    public CurrentUserImageVH(View itemView) {
        super(itemView);

        messageImageView = itemView.findViewById(R.id.current_user_image_chat_bubble);
        messageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pop the image open
            }
        });
        profileImageView = itemView.findViewById(R.id.current_user_image_profile_image_view);
        timeStampTextView = itemView.findViewById(R.id.current_user_image_time_stamp_text_view);
    }
}
