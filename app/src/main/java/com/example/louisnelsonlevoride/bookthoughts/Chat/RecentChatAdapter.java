package com.example.louisnelsonlevoride.bookthoughts.Chat;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Models.RecentChat;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.ConstantsHelperSingleton;

import java.util.Date;
import java.util.List;

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatViewHolder> {

    Context context;
    List<RecentChat> recentChats;
    String username;
    SelectRecentChatInterface selectRecentChatInterface;
    private String TAG = "RecentChatAdapter";

    public RecentChatAdapter(Context context, List<RecentChat> recentChats,String username, SelectRecentChatInterface selectRecentChatInterface) {
        this.context = context;
        this.recentChats = recentChats;
        this.username = username;
        this.selectRecentChatInterface = selectRecentChatInterface;
    }

    @NonNull
    @Override
    public RecentChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_chat_item,parent,false);
        return new RecentChatViewHolder(view,selectRecentChatInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentChatViewHolder holder, int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.defaultprofile)
                .error(R.drawable.defaultprofile);
        RecentChat recentChat = recentChats.get(position);
        if (recentChat.getSender().getUsername().equals(username)){
            User otherUser = recentChat.getReceiver();
            if (otherUser.getImageUrl()!= null){
                Glide.with(context).load(otherUser.getImageUrl()).apply(options).into(holder.profileImageView);
            }
            holder.usernameTextView.setText(otherUser.getUsername());
            if (recentChat.getRead()){
                holder.readProfileImageView.setVisibility(View.VISIBLE);
                if (otherUser.getImageUrl() != null){
                    Glide.with(context).load(otherUser.getImageUrl()).apply(options).into(holder.readProfileImageView);
                }
            }else {
                holder.readProfileImageView.setVisibility(View.INVISIBLE);
            }
        }else{
            User otherUser = recentChats.get(position).getSender();
            if (otherUser.getImageUrl() != null){
                Glide.with(context).load(otherUser.getImageUrl()).apply(options).into(holder.profileImageView);
            }
            holder.usernameTextView.setText(otherUser.getUsername());

            if (recentChat.getRead()){
                holder.readProfileImageView.setVisibility(View.INVISIBLE);
                holder.usernameTextView.setTypeface(holder.usernameTextView.getTypeface(), Typeface.NORMAL);
                holder.lastMessageTextView.setTypeface(holder.lastMessageTextView.getTypeface(), Typeface.NORMAL);
                holder.timeStampTextView.setTypeface(holder.timeStampTextView.getTypeface(), Typeface.NORMAL);
            }else{
                holder.readProfileImageView.setVisibility(View.VISIBLE);
                //username,last message,timestamp bold
                //blue indication image
                holder.usernameTextView.setTypeface(holder.usernameTextView.getTypeface(), Typeface.BOLD);
                holder.lastMessageTextView.setTypeface(holder.lastMessageTextView.getTypeface(), Typeface.BOLD);
                holder.timeStampTextView.setTypeface(holder.timeStampTextView.getTypeface(), Typeface.BOLD);
                if (recentChat.getText() == null){
                    holder.readProfileImageView.setImageResource(R.drawable.mimage);
                }else {
                    holder.readProfileImageView.setImageResource(R.drawable.mtext);
                }
            }
        }

        if (recentChat.getText() == null){
            holder.lastMessageTextView.setText(R.string.new_thought_image);
        }else{
            holder.lastMessageTextView.setText(recentChats.get(position).getText());
        }
        Date tempDate = ConstantsHelperSingleton.getInstance().formatMongodbDateToDate(recentChats.get(position).getTimestamp());
        holder.timeStampTextView.setText(ConstantsHelperSingleton.getInstance().formatDateToRecentString(tempDate,false));
    }

    @Override
    public int getItemCount() {
        if(recentChats != null)
            return recentChats.size();
        else
            return 0;
    }
}
