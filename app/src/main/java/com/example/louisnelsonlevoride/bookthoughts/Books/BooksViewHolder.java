package com.example.louisnelsonlevoride.bookthoughts.Books;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.Books.Chapters.SelectChapterInterface;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;

import java.util.List;

public class BooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView bookImage;
    public ImageView favouriteImage;
    public TextView bookTitle;
    public TextView bookAuthor;
    public TextView bookTimestamp;
    public SelectBookInterface selectBookInterface;
    public Boolean editBook;
    public List<Book> books;
    public Context context;
    DialogInterface dialogInterface;

    public BooksViewHolder(View itemView, SelectBookInterface selectBookInterface, Boolean editBook, List<Book> books, Context context,DialogInterface dialogInterface) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.selectBookInterface = selectBookInterface;
        bookImage = itemView.findViewById(R.id.book_image);
        bookTitle = itemView.findViewById(R.id.book_title);
        bookAuthor = itemView.findViewById(R.id.book_author);
        bookTimestamp = itemView.findViewById(R.id.book_timestamp);
        favouriteImage = itemView.findViewById(R.id.favourite_image_view);
        this.books = books;
        this.context = context;
        this.dialogInterface = dialogInterface;
        favouriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFavourite();
            }
        });
        this.editBook = editBook;

        if (books == null){
            favouriteImage.setVisibility(View.INVISIBLE);
        }else{
            favouriteImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (editBook){
            selectBookInterface.selectEditBook();
        }else{
            selectBookInterface.selectBook(getAdapterPosition());
        }
    }

    public void saveFavourite(){
        int position = getAdapterPosition();
        if (books != null){
            Book book = books.get(position);
            CurrentUserSingleton.getInstance().setFavouriteBook(context,book.getId());
            CurrentUserSingleton.getInstance().setFavouriteBookTitle(context,book.getTitle());
            CurrentUserSingleton.getInstance().setFavouriteBookImageUrl(context,book.getImageUrl());
            dialogInterface.showDialog(book);
        }

    }
}
