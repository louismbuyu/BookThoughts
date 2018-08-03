package com.example.louisnelsonlevoride.bookthoughts.Users;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.R;

public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView profileImage;
    TextView usernameTextView;
    TextView displayNameTextView;
    SelectUserInterface selectUserInterface;

    public UsersViewHolder(View itemView, SelectUserInterface selectUserInterface) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.selectUserInterface = selectUserInterface;
        profileImage = itemView.findViewById(R.id.user_profile_image);
        usernameTextView = itemView.findViewById(R.id.user_username_text_view);
        displayNameTextView = itemView.findViewById(R.id.user_display_name_text_view);
    }

    @Override
    public void onClick(View view) {
        selectUserInterface.selectUser(getAdapterPosition());
    }
}
