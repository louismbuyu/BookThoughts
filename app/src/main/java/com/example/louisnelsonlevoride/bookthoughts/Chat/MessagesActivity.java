package com.example.louisnelsonlevoride.bookthoughts.Chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.louisnelsonlevoride.bookthoughts.Books.AddEditBookActivity;
import com.example.louisnelsonlevoride.bookthoughts.Models.Message;
import com.example.louisnelsonlevoride.bookthoughts.Models.RecentChat;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreate;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseMessage;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseMessages;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseRecentChats;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;
import com.example.louisnelsonlevoride.bookthoughts.NavigationActivity;
import com.example.louisnelsonlevoride.bookthoughts.R;
import com.example.louisnelsonlevoride.bookthoughts.Services.MessageClient;
import com.example.louisnelsonlevoride.bookthoughts.Singletons.CurrentUserSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessagesActivity extends AppCompatActivity implements SelectMessageInterface {

    String chatRoomId;
    RecyclerView recyclerView;
    EditText inputEditText;
    Button sendBtn;
    MessagesAdapter messagesAdapter;
    List<Message> messages;
    String userId;
    String TAG = "MessagesActivity";
    User currentUser;
    User otherUser;
    Toolbar toolbar;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        recyclerView = findViewById(R.id.messages_recycle_view);
        toolbar = findViewById(R.id.messages_toolbar);

        setSupportActionBar(toolbar);
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop,int oldRight, int oldBottom)
            {
                if (recyclerView != null && messages != null){
                    recyclerView.scrollToPosition(messages.size()-1);
                }
            }
        });

        inputEditText = findViewById(R.id.chat_input_edit_text);
        sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputEditText.getText().toString().isEmpty()){
                    sendAction(inputEditText.getText().toString());
                }else {

                }

            }
        });
        userId = CurrentUserSingleton.getInstance().getUserId(this);
        String tempChatRoomId = getIntent().getStringExtra("chatRoomId");
        if (tempChatRoomId != null){
            chatRoomId = tempChatRoomId;
            getChatRoomMessages(chatRoomId);
        }

        User tempCurrentUser = getIntent().getParcelableExtra("currentUser");
        if (tempCurrentUser != null){
            currentUser = tempCurrentUser;
        }

        User tempOtherUser = getIntent().getParcelableExtra("otherUser");
        if (tempOtherUser != null){
            otherUser = tempOtherUser;
            setTitle(otherUser.getDisplayName());
        }
        setupUpProfileImage();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        addSocketListener();
    }

    private void setupUpProfileImage(){
        profileImage = findViewById(R.id.toolbar_image);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.defaultprofile)
                .error(R.drawable.defaultprofile);
        Glide.with(this).load(otherUser.getImageUrl()).apply(options).into(profileImage);
    }

    private void addSocketListener(){
        // Receiving an object
        try {
            final Socket socket;
            socket = IO.socket("https://guarded-ridge-59458.herokuapp.com/");
            socket.on(userId, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    int emitType = (int) args[0];
                    if (emitType == 1){
                        String mChatRoomId = (String) args[1];
                        JSONObject jsonObject = (JSONObject) args[2];

                        JSONObject senderObject = (JSONObject) args[3];
                        JSONObject receiverObject = (JSONObject) args[4];

                        if (chatRoomId.equals(mChatRoomId)){
                            Message message = parseJSON(jsonObject,parseUser(senderObject),parseUser(receiverObject));
                            if (message != null){
                                messages.add(message);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messagesAdapter.notifyDataSetChanged();
                                        recyclerView.scrollToPosition(messages.size() - 1);
                                    }
                                });
                            }else{

                            }
                        }
                    }
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Message parseJSON(JSONObject jsonObject,User senderUser, User receiverUser){
        String _id;
        String chatRoomId;
        String text = null;
        String imageUrl = null;
        String created_at;
        Boolean read;
        Message message = null;
        try {
            _id  = jsonObject.getString("_id");
            readMessage(_id);
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
                message = new Message(_id,text,imageUrl,chatRoomId,senderUser,receiverUser,
                        senderUser.getUserId(),receiverUser.getUserId(),created_at,read);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    private User parseUser(JSONObject jsonObject){
        User user = null;
        String _id;
        String jUsername;
        String displayName;
        String imageUrl;
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

    private void readMessage(String messageId){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/message/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        MessageClient client = retrofit.create(MessageClient.class);
        Call<ResponseCreate> call = client.readMessage(messageId);
        call.enqueue(new Callback<ResponseCreate>() {
            @Override
            public void onResponse(Call<ResponseCreate> call, Response<ResponseCreate> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().getSuccess()){

                    }else{

                    }
                }else{

                }
            }
            @Override
            public void onFailure(Call<ResponseCreate> call, Throwable t) {

            }
        });
    }

    private void setupViews(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        messagesAdapter = new MessagesAdapter(this,messages,userId,this,currentUser.getImageUrl(),otherUser.getImageUrl());
        recyclerView.setAdapter(messagesAdapter);
        recyclerView.scrollToPosition(messages.size() - 1);
    }

    private void getChatRoomMessages(String chatRoomId){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/message/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        MessageClient client = retrofit.create(MessageClient.class);
        Call<ResponseMessages> call = client.getChatRoomMessages(chatRoomId, userId);
        call.enqueue(new Callback<ResponseMessages>() {
            @Override
            public void onResponse(Call<ResponseMessages> call, Response<ResponseMessages> response) {
                if (response.isSuccessful() && (response.body()!=null)){
                    ResponseMessages responseMessages = response.body();
                    if (responseMessages.getSuccess()){
                        messages = responseMessages.getMessages();
                        setupViews();
                    }else{
                        //Log.i(TAG,"This is an error 1");
                    }
                }else{
                    //Log.i(TAG,"This is an error 2");
                }
            }
            @Override
            public void onFailure(Call<ResponseMessages> call, Throwable t) {
                //Log.i(TAG,"Error: "+t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void selectMessage(int position) {

    }

    private void sendAction(String message){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://guarded-ridge-59458.herokuapp.com/message/v0/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        MessageClient client = retrofit.create(MessageClient.class);
        Call<ResponseMessage> call = client.createMessage(currentUser.getUserId(),otherUser.getUserId(),"",message);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful() && (response.body()!=null)){
                    ResponseMessage responseMessage = response.body();
                    if (responseMessage.getSuccess()){
                        messages.add(responseMessage.getSavedMessage());
                        messagesAdapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(messages.size() - 1);
                        inputEditText.setText("");
                    }else{
                        //Log.i(TAG,"This is an error");
                    }
                }else{
                    //Log.i(TAG,"This is an error");
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                //Log.i(TAG,"Error: "+t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MessagesActivity.this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent,2);
        setResult(RESULT_OK);
        finish();
    }
}
