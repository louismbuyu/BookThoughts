package com.example.louisnelsonlevoride.bookthoughts.Books.Quotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.louisnelsonlevoride.bookthoughts.R;

public class AddQuoteViewHolder extends RecyclerView.ViewHolder {

    Button button;
    AddQuoteInterface addQuoteInterface;

    public AddQuoteViewHolder(View itemView,AddQuoteInterface addQuoteInterface ) {
        super(itemView);
        this.addQuoteInterface = addQuoteInterface;
        button = itemView.findViewById(R.id.add_new_quote_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuoteAction();
            }
        });
    }

    private void addQuoteAction(){
        addQuoteInterface.addNewQuote();
    }
}
