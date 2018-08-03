package com.example.louisnelsonlevoride.bookthoughts.Services;

import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreate;
import com.example.louisnelsonlevoride.bookthoughts.Models.RecentChat;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseMessage;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseMessages;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseRecentChats;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MessageClient {

    @POST("create")
    @FormUrlEncoded
    Call<ResponseMessage> createMessage(@Field("senderId") String senderId,
                                        @Field("receiverId") String receiverId,
                                        @Field("imageUrl") String imageUrl,
                                        @Field("text") String text);

    @POST("retrieve/recent")
    @FormUrlEncoded
    Call<ResponseRecentChats> getRecentChats(@Field("userId") String userId);

    @POST("retrieve/chatroom")
    @FormUrlEncoded
    Call<ResponseMessages> getChatRoomMessages(@Field("chatRoomId") String chatRoomId,
                                               @Field("userId") String userId);

    @POST("read")
    @FormUrlEncoded
    Call<ResponseCreate> readMessage(@Field("messageId") String messageId);
}
