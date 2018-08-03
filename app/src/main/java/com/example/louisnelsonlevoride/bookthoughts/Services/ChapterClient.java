package com.example.louisnelsonlevoride.bookthoughts.Services;

import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseBooks;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseChapters;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreate;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ChapterClient {
    @POST("retrieve")
    @FormUrlEncoded
    Call<ResponseChapters> getBooksChapters(@Field("bookId") String bookId);

    @POST("create")
    @FormUrlEncoded
    Call<ResponseCreate> createChapter(@Field("bookId") String bookId,
                                     @Field("username") String username,
                                     @Field("title") String title,
                                     @Field("number") int number);

    @POST("update")
    @FormUrlEncoded
    Call<ResponseCreate> updateChapter(@Field("chapterId") String chapterId,
                                       @Field("title") String title,
                                       @Field("number") int number);

    @POST("delete")
    @FormUrlEncoded
    Call<ResponseCreate> deleteChapter(@Field("chapterId") String chapterId);
}
