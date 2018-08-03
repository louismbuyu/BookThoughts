package com.example.louisnelsonlevoride.bookthoughts.Books.Chapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.louisnelsonlevoride.bookthoughts.R;

public class AddChapterViewHolder extends RecyclerView.ViewHolder {

    public Button button;
    AddChapterInterface addChapterInterface;

    public AddChapterViewHolder(View itemView,AddChapterInterface addChapterInterface) {
        super(itemView);
        this.addChapterInterface = addChapterInterface;
        button = itemView.findViewById(R.id.add_new_chapter_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAction();
            }
        });
    }

    private void addAction(){
        addChapterInterface.addNewChapter();
    }
}
