package com.example.louisnelsonlevoride.bookthoughts.Profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.R;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    User user;
    SelectProfileTabInterface selectProfileTabInterface;

    public ProfileAdapter(Context context,User user, SelectProfileTabInterface selectProfileTabInterface) {
        this.context = context;
        this.user = user;
        this.selectProfileTabInterface = selectProfileTabInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View profile_tab_profile_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_tab_profile_item,parent,false);
        View profile_tab_settings_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_tab_settings_item,parent,false);

        if (viewType == 0){
            return new ProfileViewHolder(profile_tab_profile_view,selectProfileTabInterface);
        }else{
            return new SettingsViewHolder(profile_tab_settings_view,selectProfileTabInterface);
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
            ProfileViewHolder profileViewHolder = (ProfileViewHolder) holder;
            profileViewHolder.usernameTextView.setText(user.getDisplayName());
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.defaultprofile)
                    .error(R.drawable.defaultprofile);
            Glide.with(context).load(user.getImageUrl()).apply(options).into(profileViewHolder.profileImageView);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
