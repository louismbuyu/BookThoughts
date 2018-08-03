package com.example.louisnelsonlevoride.bookthoughts.Services;

import com.example.louisnelsonlevoride.bookthoughts.Models.CreateUser;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreate;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreateUser;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseUsers;
import com.example.louisnelsonlevoride.bookthoughts.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserClient {
    @POST("create")
    @FormUrlEncoded
    Call<ResponseCreateUser> createUser(@Field("username") String username,
                                        @Field("password") String password,
                                        @Field("imageUrl") String imageUrl);

    @POST("signin")
    @FormUrlEncoded
    Call<ResponseCreateUser> signin(@Field("username") String username,
                                @Field("password") String password);

    @POST("search")
    @FormUrlEncoded
    Call<ResponseUsers> searchUser(@Field("text") String text);

    @POST("retrieve")
    @FormUrlEncoded
    Call<ResponseCreateUser> getUser(@Field("username") String username);

    @POST("update")
    @FormUrlEncoded
    Call<ResponseCreate> updateUser(@Field("displayName") String displayName,
                                    @Field("imageUrl") String imageUrl,
                                    @Field("username") String username);

    @POST("logout")
    @FormUrlEncoded
    Call<ResponseCreate> logout(@Field("username") String username);
}
