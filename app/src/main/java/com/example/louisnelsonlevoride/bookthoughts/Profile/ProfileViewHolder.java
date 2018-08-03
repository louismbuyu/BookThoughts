package com.example.louisnelsonlevoride.bookthoughts.Profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.R;

public class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView profileImageView;
    TextView usernameTextView;
    SelectProfileTabInterface selectProfileTabInterface;

    public ProfileViewHolder(View itemView, SelectProfileTabInterface selectProfileTabInterface) {
        super(itemView);
        itemView.setOnClickListener(this);
        profileImageView = itemView.findViewById(R.id.profile_tab_profile_image);
        usernameTextView = itemView.findViewById(R.id.profile_tab_username);
        this.selectProfileTabInterface = selectProfileTabInterface;
    }

    @Override
    public void onClick(View view) {
        selectProfileTabInterface.selectProfileTabItem(getAdapterPosition());
    }
}
