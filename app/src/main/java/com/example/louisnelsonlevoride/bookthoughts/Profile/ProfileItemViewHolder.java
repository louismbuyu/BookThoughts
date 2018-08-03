package com.example.louisnelsonlevoride.bookthoughts.Profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.R;

public class ProfileItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView nextIndicator;
    TextView titleTextView;
    TextView valueTextView;
    Boolean edit;
    SelectProfileItemInteface selectProfileItemInteface;

    public ProfileItemViewHolder(View itemView,Boolean edit, SelectProfileItemInteface selectProfileItemInteface) {
        super(itemView);
        itemView.setOnClickListener(this);
        nextIndicator = itemView.findViewById(R.id.next_indicator);
        titleTextView = itemView.findViewById(R.id.title_text_view);
        valueTextView = itemView.findViewById(R.id.value_text_view);
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
