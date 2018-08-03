package com.example.louisnelsonlevoride.bookthoughts.Users;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.R;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder> {

    List<User> users;
    Context context;
    SelectUserInterface selectUserInterface;

    public UsersAdapter(List<User> users, Context context,SelectUserInterface selectUserInterface) {
        this.users = users;
        this.context = context;
        this.selectUserInterface = selectUserInterface;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new UsersViewHolder(view,selectUserInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.defaultprofile)
                .error(R.drawable.defaultprofile);
        if (users.get(position).getImageUrl() == null || users.get(position).getImageUrl().isEmpty()){
            Glide.with(context).load("").apply(options).into(holder.profileImage);
        }else{
            Glide.with(context).load(users.get(position).getImageUrl()).apply(options).into(holder.profileImage);
        }
        holder.usernameTextView.setText(users.get(position).getUsername());
        holder.displayNameTextView.setText(users.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        if(users!=null){
            return users.size();
        }else {
            return 0;
        }
    }
}
