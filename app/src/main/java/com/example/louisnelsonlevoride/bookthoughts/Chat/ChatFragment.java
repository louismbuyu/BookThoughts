package com.example.louisnelsonlevoride.bookthoughts.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;
import com.example.louisnelsonlevoride.bookthoughts.Models.RecentChat;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseRecentChats;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.MessageClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatFragment extends Fragment implements SelectRecentChatInterface {

    RecyclerView recyclerView;
    List<RecentChat> recentChats;
    RecentChatAdapter recentChatAdapter;
    String userId;
    String username;
    private String TAG = "ChatFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);
        // Inflate the layout for this fragment
        getActivity().setTitle(getString(R.string.chats));
        addSocketForNewCHat();
        recyclerView = rootView.findViewById(R.id.chats_recycle_view);
        userId = CurrentUserSingleton.getInstance().getUserId(getActivity());
        username = CurrentUserSingleton.getInstance().getUsername(getActivity());
        getRecentChats();
        return rootView;
    }

    private void addSocketForNewCHat(){
        // Receiving an object
        try {
            final Socket socket;
            socket = IO.socket("https://guarded-ridge-59458.herokuapp.com/");
            String listener = CurrentUserSingleton.getInstance().getUserId(getActivity());
            socket.on(listener, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    int emitType = (int) args[0];

                    if (emitType == 1){
                        String chatRoomId = (String) args[1];
                        JSONObject jsonObject = (JSONObject) args[2];

                        JSONObject senderObject = (JSONObject) args[3];
                        JSONObject receiverObject = (JSONObject) args[4];

                        Boolean contains = false;
                        int position = 0;
                        for (int i = 0; i < recentChats.size();i++){
                            if(recentChats.get(i).get_id().equals(chatRoomId)){
                                contains = true;
                                position = i;
                            }
                        }

                        RecentChat recentChat = parseJSON(jsonObject,parseUser(senderObject),parseUser(receiverObject));
                        if (recentChat != null){
                            if (contains){
                                recentChats.remove(position);
                            }
                            recentChats.add(0,recentChat);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recentChatAdapter.notifyDataSetChanged();
                                }
                            });
                        }else{
                        }
                    }
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private RecentChat parseJSON(JSONObject jsonObject,User senderUser, User receiverUser){
        String chatRoomId = "";
        String text = null;
        String imageUrl = null;
        String created_at = "";
        Boolean read = false;
        RecentChat recentChat = null;
        try {
            chatRoomId  = jsonObject.getString("chatRoomId");
            Boolean hasText = jsonObject.has("text");
            if (hasText){
                text  = jsonObject.getString("text");
            }

            Boolean hasImageUrl = jsonObject.has("imageUrl");
            if (hasImageUrl){
                imageUrl  = jsonObject.getString("imageUrl");
            }

            created_at = jsonObject.getString("created_at");
            read = jsonObject.getBoolean("read");
            if (senderUser != null && receiverUser != null){
                recentChat = new RecentChat(chatRoomId,text,imageUrl,created_at,read,senderUser,receiverUser);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recentChat;
    }

    private User parseUser(JSONObject jsonObject){
        User user = null;
        String _id = "";
        String jUsername = "";
        String displayName = "";
        String imageUrl = "";
        try {
            jUsername = jsonObject.getString("username");
            _id = jsonObject.getString("_id");
            displayName = jsonObject.getString("displayName");
            imageUrl = jsonObject.getString("imageUrl");
            user = new User(jUsername,_id,displayName,imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    private void setupViews(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recentChatAdapter = new RecentChatAdapter(getActivity(),recentChats,username,this);
        recyclerView.setAdapter(recentChatAdapter);
    }

    private void getRecentChats(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/message/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        MessageClient client = retrofit.create(MessageClient.class);
        Call<ResponseRecentChats> call = client.getRecentChats(userId);
        call.enqueue(new Callback<ResponseRecentChats>() {
            @Override
            public void onResponse(Call<ResponseRecentChats> call, Response<ResponseRecentChats> response) {
                if (response.isSuccessful() && (response.body()!=null)){
                    ResponseRecentChats responseRecentChats = response.body();
                    if (responseRecentChats.getSuccess()){
                        List<RecentChat> tempRecentChats = responseRecentChats.getRecentChats();
                        if (tempRecentChats != null){
                            Collections.reverse(tempRecentChats);
                            recentChats = tempRecentChats;
                        }else {
                            recentChats = responseRecentChats.getRecentChats();
                        }
                        setupViews();
                    }else{
                    }
                }else{
                }
            }

            @Override
            public void onFailure(Call<ResponseRecentChats> call, Throwable t) {
            }
        });
    }

    @Override
    public void selectRecentChat(int position) {
        Intent intent = new Intent(getActivity(),MessagesActivity.class);
        intent.putExtra("chatRoomId", recentChats.get(position).get_id());
        if (recentChats.get(position).getSender().getUsername().equals(username)){
            intent.putExtra("currentUser",recentChats.get(position).getSender());
            intent.putExtra("otherUser",recentChats.get(position).getReceiver());
        }else{
            intent.putExtra("otherUser",recentChats.get(position).getSender());
            intent.putExtra("currentUser",recentChats.get(position).getReceiver());
        }
        getActivity().startActivityForResult(intent,2);
    }
}
