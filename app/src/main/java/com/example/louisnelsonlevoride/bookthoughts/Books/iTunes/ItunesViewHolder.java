package com.example.louisnelsonlevoride.bookthoughts.Books.iTunes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseItunesBook;
import com.example.louisnelsonlevoride.bookthoughts.R;

import java.util.List;

public class ItunesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView bookImage;
    TextView bookTitle;
    TextView bookAuthor;
    TextView bookTimestamp;
    List<ResponseItunesBook> books;
    SelectItunesBookInterface selectItunesBookInterface;
    ImageView favouriteImage;

    public ItunesViewHolder(View itemView, List<ResponseItunesBook> books, SelectItunesBookInterface selectItunesBookInterface) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.selectItunesBookInterface = selectItunesBookInterface;
        bookImage = itemView.findViewById(R.id.book_image);
        bookTitle = itemView.findViewById(R.id.book_title);
        bookAuthor = itemView.findViewById(R.id.book_author);
        bookTimestamp = itemView.findViewById(R.id.book_timestamp);
        favouriteImage = itemView.findViewById(R.id.favourite_image_view);
        favouriteImage.setVisibility(View.INVISIBLE);
        this.books = books;
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        ResponseItunesBook currentBook = books.get(position);
        selectItunesBookInterface.selectBook(currentBook);
    }
}
