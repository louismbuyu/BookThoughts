package com.example.louisnelsonlevoride.bookthoughts.Books.Chapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Books.BooksViewHolder;
import com.example.louisnelsonlevoride.bookthoughts.Books.DialogInterface;
import com.example.louisnelsonlevoride.bookthoughts.Books.SelectBookInterface;
import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.Models.Chapter;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.ConstantsHelperSingleton;

import java.util.Date;
import java.util.List;

public class ChaptersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DialogInterface {

    List<Chapter> chapters;
    Book book;
    SelectChapterInterface selectChapterInterface;
    SelectBookInterface selectBookInterface;
    AddChapterInterface addChapterInterface;
    Context context;

    public ChaptersAdapter(Context context, List<Chapter> chapters, Book book, SelectChapterInterface selectChapterInterface,SelectBookInterface selectBookInterface,AddChapterInterface addChapterInterface) {
        this.chapters = chapters;
        this.book = book;
        this.selectChapterInterface = selectChapterInterface;
        this.selectBookInterface = selectBookInterface;
        this.addChapterInterface = addChapterInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chapter_item_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item,parent,false);
        View book_item_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);
        View add_chapter_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_chapter_item,parent,false);

        if (viewType == 0){
            return new BooksViewHolder(book_item_view,selectBookInterface,true,null,context,this);
        }else if (viewType == 1){
            return new ChaptersViewHolder(chapter_item_view,selectChapterInterface);
        }else{
            return new AddChapterViewHolder(add_chapter_item,addChapterInterface);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }else if (position == chapters.size() + 1) {
            return 2;
        }else{
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0){
            if (holder.getItemViewType() == 0){
                BooksViewHolder booksViewHolder = (BooksViewHolder) holder;
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.defaultbook)
                        .error(R.drawable.defaultbook);
                Glide.with(context).load(book.getImageUrl()).apply(options).into(booksViewHolder.bookImage);
                booksViewHolder.bookTitle.setText(book.getTitle());
                booksViewHolder.bookAuthor.setText(book.getAuthor());
                Date tempDate = ConstantsHelperSingleton.getInstance().formatMongodbDateToDate(book.getTimeStamp());
                booksViewHolder.bookTimestamp.setText(ConstantsHelperSingleton.getInstance().formatDateToRecentString(tempDate,true));
            }
        }else if (position == chapters.size() + 1){

        }else{
            if (holder.getItemViewType() == 1) {
                ChaptersViewHolder chaptersViewHolder = (ChaptersViewHolder) holder;
                Chapter mChapter = chapters.get(position - 1);
                String chapter = context.getString(R.string.chapter);
                String placeHolder = chapter+" "+mChapter.getNumber()+": "+mChapter.getTitle();
                chaptersViewHolder.chapterTitleTextView.setText(placeHolder);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(chapters != null)
            return chapters.size() + 2;
        else
            return 2;
    }

    @Override
    public void showDialog(Book book) {

    }
}
