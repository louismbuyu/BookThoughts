package com.example.louisnelsonlevoride.bookthoughts.Books.iTunes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Books.CustomBook.CustomBookInterface;
import com.example.louisnelsonlevoride.bookthoughts.Books.CustomBook.CustomBookItemViewHolder;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseItunesBook;
import com.example.louisnelsonlevoride.bookthoughts.R;

import java.util.List;

public class ItunesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ResponseItunesBook> books;
    CustomBookInterface customBookInterface;
    SelectItunesBookInterface selectItunesBookInterface;

    public ItunesAdapter(Context context, List<ResponseItunesBook> books, CustomBookInterface customBookInterface, SelectItunesBookInterface selectItunesBookInterface) {
        this.context = context;
        this.books = books;
        this.customBookInterface = customBookInterface;
        this.selectItunesBookInterface = selectItunesBookInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_book_item,parent,false);

        if (viewType == 0){
            return new ItunesViewHolder(view, books, selectItunesBookInterface);
        }else{
            return new CustomBookItemViewHolder(view1,context,customBookInterface);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < (books.size())){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {

        if(books != null)
            return books.size() + 1;
        else
            return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (position < (books.size())){
            if (holder.getItemViewType() == 0){
                ItunesViewHolder itunesViewHolder = (ItunesViewHolder) holder;
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.defaultbook)
                        .error(R.drawable.defaultbook);
                Glide.with(context).load(books.get(position).getArtworkUrl100()).apply(options).into(itunesViewHolder.bookImage);
                itunesViewHolder.bookTitle.setText(books.get(position).getTrackName());
                itunesViewHolder.bookAuthor.setText(books.get(position).getArtistName());
                itunesViewHolder.bookTimestamp.setText(books.get(position).getReleaseDate());
            }
        }

    }
}
