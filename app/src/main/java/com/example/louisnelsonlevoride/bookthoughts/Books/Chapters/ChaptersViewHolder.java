package com.example.louisnelsonlevoride.bookthoughts.Books.Chapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.louisnelsonlevoride.bookthoughts.Models.Chapter;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseItunesBook;
import com.example.louisnelsonlevoride.bookthoughts.R;

import java.util.List;

public class ChaptersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView chapterTitleTextView;
    SelectChapterInterface selectChapterInterface;
    String TAG = "ChaptersViewHolder";

    public ChaptersViewHolder(View itemView,SelectChapterInterface selectChapterInterface) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.selectChapterInterface = selectChapterInterface;
        chapterTitleTextView = itemView.findViewById(R.id.chapter_title);
    }

    @Override
    public void onClick(View view) {
        selectChapterInterface.selectChapter(getAdapterPosition() - 1);
        selectChapterInterface.editChapter();
    }
}
