package com.example.louisnelsonlevoride.bookthoughts.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.louisnelsonlevoride.bookthoughts.Chat.RecentChatAdapter;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreateUser;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseMessages;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.MessageClient;
import com.example.louisnelsonlevoride.bookthoughts.Services.UserClient;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment implements SelectProfileTabInterface {

    RecyclerView recyclerView;
    ProfileAdapter profileAdapter;
    String userId;
    String username;
    User user;
    private String TAG = "ProfileFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        getActivity().setTitle(getString(R.string.profile));
        recyclerView = rootView.findViewById(R.id.profile_recycle_view);
        userId = CurrentUserSingleton.getInstance().getUserId(getActivity());
        username = CurrentUserSingleton.getInstance().getUsername(getActivity());
        getCurrentUserInfo();
        return rootView;
    }

    private void getCurrentUserInfo(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/user/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<ResponseCreateUser> call = client.getUser(username);
        call.enqueue(new Callback<ResponseCreateUser>() {
            @Override
            public void onResponse(Call<ResponseCreateUser> call, Response<ResponseCreateUser> response) {
                if (response.body().getSuccess()){
                    user = response.body().getUser();
                    setupViews();
                }else{

                }
            }
            @Override
            public void onFailure(Call<ResponseCreateUser> call, Throwable t) {

            }
        });
    }

    private void setupViews(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        profileAdapter = new ProfileAdapter(getActivity(),user,this);
        recyclerView.setAdapter(profileAdapter);
    }

    @Override
    public void selectProfileTabItem(int position) {
        if (position == 0){
            Intent intent = new Intent(getActivity(),ProfileActivity.class);
            intent.putExtra("user",user);
            startActivityForResult(intent,1);
        }else{
            Intent intent = new Intent(getActivity(),SettingsActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getCurrentUserInfo();
        }

    }
}
