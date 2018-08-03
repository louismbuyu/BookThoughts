package com.example.louisnelsonlevoride.bookthoughts.Books.Quotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.Models.Quote;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseItunesBook;
import com.example.louisnelsonlevoride.bookthoughts.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    List<Quote> quotes;
    TextView timeStampTextView;
    TextView thoughtTextView;
    ImageView quoteImageView;
    TextView quoteNumberTextView;
    SelectQuoteInterface selectQuoteInterface;

    public QuotesViewHolder(View itemView, List<Quote> quotes,SelectQuoteInterface selectQuoteInterface) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.quotes = quotes;
        timeStampTextView = itemView.findViewById(R.id.timestamp_text);
        thoughtTextView = itemView.findViewById(R.id.thought_text);
        quoteImageView = itemView.findViewById(R.id.quote_image);
        quoteNumberTextView = itemView.findViewById(R.id.quote_number_text_view);
        this.selectQuoteInterface = selectQuoteInterface;
    }

    @Override
    public void onClick(View view) {
        selectQuoteInterface.selectQuote(getAdapterPosition() - 1);
    }
}
