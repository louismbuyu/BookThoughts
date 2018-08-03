package com.example.louisnelsonlevoride.bookthoughts.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.R;

public class CurrentUserMessageVH extends RecyclerView.ViewHolder {

    ImageView profileImageView;
    TextView messageTextView;
    TextView timeStampTextview;

    public CurrentUserMessageVH(View itemView) {
        super(itemView);

        profileImageView = itemView.findViewById(R.id.current_user_message_profile_image_view);
        messageTextView = itemView.findViewById(R.id.current_user_message_chat_bubble);
        timeStampTextview = itemView.findViewById(R.id.current_user_message_time_stamp_text_view);
    }
}
