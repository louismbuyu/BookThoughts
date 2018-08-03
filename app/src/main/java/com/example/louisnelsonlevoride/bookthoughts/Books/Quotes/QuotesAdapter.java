package com.example.louisnelsonlevoride.bookthoughts.Books.Quotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Books.BooksViewHolder;
import com.example.louisnelsonlevoride.bookthoughts.Books.Chapters.AddChapterViewHolder;
import com.example.louisnelsonlevoride.bookthoughts.Books.Chapters.ChaptersViewHolder;
import com.example.louisnelsonlevoride.bookthoughts.Books.Chapters.SelectChapterInterface;
import com.example.louisnelsonlevoride.bookthoughts.Models.Chapter;
import com.example.louisnelsonlevoride.bookthoughts.Models.Quote;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.ConstantsHelperSingleton;

import java.util.Date;
import java.util.List;

public class QuotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    SelectChapterInterface selectChapterInterface;
    SelectQuoteInterface selectQuoteInterface;
    AddQuoteInterface addQuoteInterface;
    List<Quote> quotes;
    Chapter chapter;
    Context context;

    public QuotesAdapter(SelectChapterInterface selectChapterInterface,
                         List<Quote> quotes, Context context,
                         SelectQuoteInterface selectQuoteInterface,
                         AddQuoteInterface addQuoteInterface,Chapter chapter) {
        this.selectChapterInterface = selectChapterInterface;
        this.selectQuoteInterface = selectQuoteInterface;
        this.quotes = quotes;
        this.chapter = chapter;
        this.context = context;
        this.addQuoteInterface = addQuoteInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chapter_item_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item,parent,false);
        View quote_item_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_item,parent,false);
        View add_quote_item_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_quote_item,parent,false);
        if (viewType == 0){
            return new ChaptersViewHolder(chapter_item_view,selectChapterInterface);
        }else if (viewType == 1){
            return new QuotesViewHolder(quote_item_view,quotes,selectQuoteInterface);
        }else{
            return new AddQuoteViewHolder(add_quote_item_view,addQuoteInterface);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }else if (position == quotes.size() + 1) {
            return 2;
        }else{
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0){
            if (holder.getItemViewType() == 0){
                ChaptersViewHolder chaptersViewHolder = (ChaptersViewHolder) holder;
                String placeHolder = R.string.chapter+" "+chapter.getNumber()+": "+chapter.getTitle();
                chaptersViewHolder.chapterTitleTextView.setText(placeHolder);
            }
        }else if (position == quotes.size() + 1){

        }else{
            if (holder.getItemViewType() == 1) {
                QuotesViewHolder quotesViewHolder = (QuotesViewHolder) holder;
                int currentPosition = position - 1 ;
                Quote mQuote = quotes.get(currentPosition);
                String placeHolder = "#"+position;
                quotesViewHolder.quoteNumberTextView.setText(placeHolder);
                Date tempDate = ConstantsHelperSingleton.getInstance().formatMongodbDateToDate(mQuote.getUpdated_at());
                quotesViewHolder.timeStampTextView.setText(ConstantsHelperSingleton.getInstance().formatDateToRecentString(tempDate,true));
                quotesViewHolder.thoughtTextView.setText(mQuote.getThought());
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.defaultbook)
                        .error(R.drawable.defaultbook);
                Glide.with(context).load(mQuote.getImageUrl()).apply(options).into(quotesViewHolder.quoteImageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(quotes != null)
            return quotes.size() + 2;
        else
            return 2;
    }
}
