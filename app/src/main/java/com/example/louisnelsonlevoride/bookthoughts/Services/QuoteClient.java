package com.example.louisnelsonlevoride.bookthoughts.Services;

import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseBooks;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreate;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreateBook;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseQuotes;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface QuoteClient {
    @POST("create")
    @FormUrlEncoded
    Call<ResponseCreate> createQuote(@Field("username") String username,
                                     @Field("bookId") String bookId,
                                     @Field("chapterId") String chapterId,
                                     @Field("imageUrl") String imageUrl,
                                     @Field("thought") String thought);

    @POST("update")
    @FormUrlEncoded
    Call<ResponseCreate> updateQuote(@Field("quoteId") String quoteId,
                                         @Field("imageUrl") String imageUrl,
                                         @Field("thought") String thought);

    @Multipart
    @PUT
    Call<ResponseBody> uploadImage(@Header("Content-Type") String type, @Url String postUrl, @Part MultipartBody.Part image);

    @POST("retrieve")
    @FormUrlEncoded
    Call<ResponseQuotes> getQuotes(@Field("chapterId") String chapterId);

    @POST("delete")
    @FormUrlEncoded
    Call<ResponseCreate> deleteQuote(@Field("quoteId") String quoteId);
}
