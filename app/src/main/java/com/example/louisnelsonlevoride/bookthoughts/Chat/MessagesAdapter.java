package com.example.louisnelsonlevoride.bookthoughts.Chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Models.Message;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.ConstantsHelperSingleton;

import java.util.Date;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Message> messages;
    String userId;
    SelectMessageInterface selectMessageInterface;
    Context context;
    String currentUserImageUrl;
    String otherUserImageUrl;
    private String TAG = "MessagesAdapter";

    public MessagesAdapter(Context context,List<Message> messages, String userId, SelectMessageInterface selectMessageInterface, String currentUserImageUrl, String otherUserImageUrl) {
        this.context = context;
        this.messages = messages;
        this.userId = userId;
        this.selectMessageInterface = selectMessageInterface;
        this.currentUserImageUrl = currentUserImageUrl;
        this.otherUserImageUrl = otherUserImageUrl;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View currentUserTextVH = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_user_message_item,parent,false);
        View currentUserImageVH = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_user_image_item,parent,false);
        View otherUserTextVH = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_user_message_item,parent,false);
        View otherUserImageVH = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_user_image_item,parent,false);

        if (viewType == 0){
            return new CurrentUserMessageVH(currentUserTextVH);
        }else if (viewType == 1){
            return new CurrentUserImageVH(currentUserImageVH);
        }else if (viewType == 2){
            return new OtherUserMessageVH(otherUserTextVH);
        }else if (viewType == 3){
            return new OtherUserImageVH(otherUserImageVH);
        }else{
            return new CurrentUserMessageVH(currentUserTextVH);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderUser().getUserId().equals(userId)){
            if (messages.get(position).getImageUrl() == null){
                return 0;
            }else{
                if (messages.get(position).getImageUrl().toString().isEmpty()){
                    return 0;
                }else{
                    return 1;
                }
            }
        }else{
            if (messages.get(position).getImageUrl() == null){
                return 2;
            }else{
                if (messages.get(position).getImageUrl().toString().isEmpty()){
                    return 2;
                }else{
                    return 3;
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = holder.getItemViewType();
        Message currentMessage = messages.get(position);
        if (viewType == 0){
            CurrentUserMessageVH currentUserMessageVH = (CurrentUserMessageVH) holder;
            currentUserMessageVH.messageTextView.setText(currentMessage.getText());
            Date tempDate = ConstantsHelperSingleton.getInstance().formatMongodbDateToDate(currentMessage.getCreated_at());
            currentUserMessageVH.timeStampTextview.setText(ConstantsHelperSingleton.getInstance().formatDateToRecentString(tempDate,true));
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.defaultprofile)
                    .error(R.drawable.defaultprofile);
            Glide.with(context).load(currentUserImageUrl).apply(options).into(currentUserMessageVH.profileImageView);
        }else if (viewType == 1){
            CurrentUserImageVH currentUserImageVH = (CurrentUserImageVH) holder;

            Date tempDate = ConstantsHelperSingleton.getInstance().formatMongodbDateToDate(currentMessage.getCreated_at());
            currentUserImageVH.timeStampTextView.setText(ConstantsHelperSingleton.getInstance().formatDateToRecentString(tempDate,true));
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.defaultprofile)
                    .error(R.drawable.defaultprofile);
            Glide.with(context).load(currentUserImageUrl).apply(options).into(currentUserImageVH.profileImageView);
            RequestOptions options1 = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.defaultprofile)
                    .error(R.drawable.defaultprofile);
            Glide.with(context).load(currentMessage.getImageUrl()).into(currentUserImageVH.messageImageView);
        }else if (viewType == 2){
            OtherUserMessageVH otherUserMessageVH = (OtherUserMessageVH) holder;
            otherUserMessageVH.messageTextView.setText(currentMessage.getText());
            Date tempDate = ConstantsHelperSingleton.getInstance().formatMongodbDateToDate(currentMessage.getCreated_at());
            otherUserMessageVH.timeStampTextview.setText(ConstantsHelperSingleton.getInstance().formatDateToRecentString(tempDate,true));
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.defaultprofile)
                    .error(R.drawable.defaultprofile);
            Glide.with(context).load(otherUserImageUrl).apply(options).into(otherUserMessageVH.profileImageView);
        }else if (viewType == 3){
            OtherUserImageVH otherUserImageVH = (OtherUserImageVH) holder;

            Date tempDate = ConstantsHelperSingleton.getInstance().formatMongodbDateToDate(currentMessage.getCreated_at());
            otherUserImageVH.timeStampTextView.setText(ConstantsHelperSingleton.getInstance().formatDateToRecentString(tempDate,true));
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.defaultprofile)
                    .error(R.drawable.defaultprofile);
            Glide.with(context).load(otherUserImageUrl).apply(options).into(otherUserImageVH.profileImageView);

            RequestOptions options1 = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.defaultprofile)
                    .error(R.drawable.defaultprofile);
            Glide.with(context).load(currentMessage.getImageUrl()).into(otherUserImageVH.messageImageView);
        }
    }

    @Override
    public int getItemCount() {
        if(messages != null)
            return messages.size();
        else
            return 0;
    }
}
