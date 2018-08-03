package com.example.louisnelsonlevoride.bookthoughts.Profile;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.R;

public class ProfileViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    User user;
    SelectProfileItemInteface selectProfileItemInteface;
    Boolean edit;
    Uri imageUri;
    String displayName;

    public ProfileViewAdapter(Context context, User user,SelectProfileItemInteface selectProfileItemInteface,Boolean edit,Uri imageUri,String displayName) {
        this.context = context;
        this.user = user;
        this.selectProfileItemInteface = selectProfileItemInteface;
        this.edit = edit;
        this.imageUri = imageUri;
        this.displayName = displayName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View profile_view_image_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_view_image_item,parent,false);
        View profile_view_text_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_view_text_item,parent,false);

        if (viewType == 0){
            return new ProfileImageViewHolder(profile_view_image_view,edit,selectProfileItemInteface);
        }else{
            return new ProfileItemViewHolder(profile_view_text_view,edit,selectProfileItemInteface);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (position == 0){
            ProfileImageViewHolder profileImageViewHolder = (ProfileImageViewHolder) holder;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.defaultprofile)
                    .error(R.drawable.defaultprofile);
            Glide.with(context).load(imageUri).apply(options).into(profileImageViewHolder.imageView);
        }else if (position == 1) {

            ProfileItemViewHolder profileItemViewHolder = (ProfileItemViewHolder) holder;
            profileItemViewHolder.titleTextView.setText(R.string.username);
            profileItemViewHolder.valueTextView.setText(user.getUsername());
            profileItemViewHolder.nextIndicator.setVisibility(View.INVISIBLE);
        }else if (position == 2) {
            ProfileItemViewHolder profileItemViewHolder = (ProfileItemViewHolder) holder;
            profileItemViewHolder.titleTextView.setText(R.string.display_name);
            profileItemViewHolder.valueTextView.setText(displayName);
            if (edit){
                profileItemViewHolder.nextIndicator.setVisibility(View.VISIBLE);
            }else{
                profileItemViewHolder.nextIndicator.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
