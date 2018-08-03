package com.example.louisnelsonlevoride.bookthoughts.Profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.louisnelsonlevoride.bookthoughts.R;

public class ProfileImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView imageView;
    Boolean edit;
    SelectProfileItemInteface selectProfileItemInteface;

    public ProfileImageViewHolder(View itemView,Boolean edit, SelectProfileItemInteface selectProfileItemInteface) {
        super(itemView);
        itemView.setOnClickListener(this);
        imageView = itemView.findViewById(R.id.profile_image);
        this.edit = edit;
        this.selectProfileItemInteface = selectProfileItemInteface;
    }

    @Override
    public void onClick(View view) {
        if (edit){
            selectProfileItemInteface.selectItem(getAdapterPosition());
        }
    }
}
