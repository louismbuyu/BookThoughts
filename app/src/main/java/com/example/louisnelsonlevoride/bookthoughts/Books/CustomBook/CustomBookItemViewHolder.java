package com.example.louisnelsonlevoride.bookthoughts.Books.CustomBook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.louisnelsonlevoride.bookthoughts.R;

public class CustomBookItemViewHolder extends RecyclerView.ViewHolder {

    Button customBookBtn;
    Context context;
    CustomBookInterface customBookInterface;
    public CustomBookItemViewHolder(View itemView, Context context,CustomBookInterface customBookInterface) {
        super(itemView);
        this.context = context;
        customBookBtn = itemView.findViewById(R.id.custom_book_button);
        this.customBookInterface = customBookInterface;
        customBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customBookAction();
            }
        });
    }

    private void customBookAction(){
        customBookInterface.transitionToCustomActivity();
    }
}
