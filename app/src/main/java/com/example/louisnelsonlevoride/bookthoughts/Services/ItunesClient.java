package com.example.louisnelsonlevoride.bookthoughts.Services;

import com.example.louisnelsonlevoride.bookthoughts.Models.Book;
import com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels.ResponseItunesSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ItunesClient {
    @GET("search?media=ebook")
    Call<ResponseItunesSearch> getBooks(@Query("term") String title);
}
