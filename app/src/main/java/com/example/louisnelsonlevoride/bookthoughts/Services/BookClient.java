package com.example.louisnelsonlevoride.bookthoughts.Services;

import android.net.Uri;

import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.Models.CreateUser;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseBooks;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreateBook;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseCreateUser;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface BookClient {

    @POST("create/image")
    @FormUrlEncoded
    Call<Book> createBookWithImageUrl(@Field("username") String username,
                                   @Field("title") String title,
                                   @Field("author") String author,
                                   @Field("imageUrl") String imageUrl);

    @POST("create/image")
    @FormUrlEncoded
    Call<ResponseCreateBook> createBookWithImageUrlFirebase(@Field("username") String username,
                                      @Field("title") String title,
                                      @Field("author") String author,
                                      @Field("imageUrl") String imageUrl);

    @POST("create")
    @FormUrlEncoded
    Call<ResponseCreateBook> createBookWithImage(@Field("username") String username,
                                                 @Field("title") String title,
                                                 @Field("author") String author);

    @POST("update")
    @FormUrlEncoded
    Call<ResponseCreateBook> updateBook(@Field("bookId") String bookId,
                                        @Field("title") String title,
                                        @Field("author") String author,
                                        @Field("imageUrl") String imageUrl);

    @POST("delete")
    @FormUrlEncoded
    Call<ResponseCreateBook> deleteBook(@Field("bookId") String bookId);

    @Multipart
    @PUT
    Call<ResponseBody> uploadPlease(@Header("Content-Type") String type,@Url String postUrl, @Part MultipartBody.Part image);

    @POST("retrieve")
    @FormUrlEncoded
    Call<ResponseBooks> getUsersBooks(@Field("userId") String userId);

    @POST("retrieve/date")
    @FormUrlEncoded
    Call<ResponseBooks> getUsersBooksWithFilter(@Field("userId") String userId, @Field("date") String date);
}
