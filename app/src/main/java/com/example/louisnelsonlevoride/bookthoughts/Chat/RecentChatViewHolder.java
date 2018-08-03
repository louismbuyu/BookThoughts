package com.example.louisnelsonlevoride.bookthoughts.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.R;

public class RecentChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView profileImageView;
    TextView usernameTextView;
    TextView timeStampTextView;
    TextView lastMessageTextView;
    ImageView readProfileImageView;
    SelectRecentChatInterface selectRecentChatInterface;

    public RecentChatViewHolder(View itemView, SelectRecentChatInterface selectRecentChatInterface) {
        super(itemView);
        itemView.setOnClickListener(this);
        profileImageView = itemView.findViewById(R.id.recent_image_view);
        usernameTextView = itemView.findViewById(R.id.recent_username_textview);
        timeStampTextView = itemView.findViewById(R.id.recent_time_stamp);
        lastMessageTextView = itemView.findViewById(R.id.recent_last_message_view);
        readProfileImageView = itemView.findViewById(R.id.recent_type_image);
        this.selectRecentChatInterface = selectRecentChatInterface;
    }

    @Override
    public void onClick(View view) {
        selectRecentChatInterface.selectRecentChat(getAdapterPosition());
    }
}
