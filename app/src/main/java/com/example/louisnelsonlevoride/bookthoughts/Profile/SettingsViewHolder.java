package com.example.louisnelsonlevoride.bookthoughts.Profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SettingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    SelectProfileTabInterface selectProfileTabInterface;

    public SettingsViewHolder(View itemView, SelectProfileTabInterface selectProfileTabInterface) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.selectProfileTabInterface = selectProfileTabInterface;
    }

    @Override
    public void onClick(View view) {
        selectProfileTabInterface.selectProfileTabItem(getAdapterPosition());
    }
}
