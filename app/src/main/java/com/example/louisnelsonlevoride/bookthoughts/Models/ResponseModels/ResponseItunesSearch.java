package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

import java.util.List;

public class ResponseItunesSearch {
    List<ResponseItunesBook> results;

    public ResponseItunesSearch(List<ResponseItunesBook> books) {
        this.results = books;
    }

    public List<ResponseItunesBook> getBooks() {
        return results;
    }

    public void setBooks(List<ResponseItunesBook> books) {
        this.results = books;
    }
}
