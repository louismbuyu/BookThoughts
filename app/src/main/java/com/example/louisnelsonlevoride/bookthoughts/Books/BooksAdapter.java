package com.example.louisnelsonlevoride.bookthoughts.Books;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.ConstantsHelperSingleton;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;

import java.util.Date;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksViewHolder> {

    Context context;
    List<Book> books;
    SelectBookInterface selectBookInterface;
    String favouriteBookId;
    DialogInterface dialogInterface;

    public BooksAdapter(Context context, List<Book> books,SelectBookInterface selectBookInterface, DialogInterface dialogInterface) {
        this.context = context;
        this.books = books;
        this.selectBookInterface = selectBookInterface;
        this.dialogInterface = dialogInterface;
        String tempId = CurrentUserSingleton.getInstance().getFavouriteBook(context);
        if (tempId != null){
            favouriteBookId = tempId;
        }
    }

    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);
        return new BooksViewHolder(view,selectBookInterface,false,books,context,dialogInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.defaultbook)
                .error(R.drawable.defaultbook);
        Glide.with(context).load(books.get(position).getImageUrl()).apply(options).into(holder.bookImage);
        holder.bookTitle.setText(books.get(position).getTitle());
        holder.bookAuthor.setText(books.get(position).getAuthor());
        Date tempDate = ConstantsHelperSingleton.getInstance().formatMongodbDateToDate(books.get(position).getTimeStamp());
        holder.bookTimestamp.setText(ConstantsHelperSingleton.getInstance().formatDateToRecentString(tempDate,false));
        RequestOptions options2 = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.unfilledstar)
                .error(R.drawable.unfilledstar);
        if (favouriteBookId != null){
            if (favouriteBookId.equals(books.get(position).getId())){
                Glide.with(context).load(R.drawable.filledstar).apply(options2).into(holder.favouriteImage);
            }else{
                Glide.with(context).load(R.drawable.unfilledstar).apply(options2).into(holder.favouriteImage);
            }
        }else{
            Glide.with(context).load(R.drawable.unfilledstar).apply(options2).into(holder.favouriteImage);
        }

    }

    @Override
    public int getItemCount() {
        if(books != null)
            return books.size();
        else
            return 0;
    }
}
