package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.louisnelsonlevoride.bookthoughts.Models.Book;

public class ResponseCreateBook implements Parcelable {
    String postUrl;
    String message;
    Boolean success;
    Book book;

    public ResponseCreateBook(String postUrl,String message, Boolean success, Book book) {
        this.postUrl = postUrl;
        this.success = success;
        this.book = book;
        this.message = message;
    }

    protected ResponseCreateBook(Parcel in) {
        postUrl = in.readString();
        message = in.readString();
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
        book = in.readParcelable(Book.class.getClassLoader());
    }

    public static final Creator<ResponseCreateBook> CREATOR = new Creator<ResponseCreateBook>() {
        @Override
        public ResponseCreateBook createFromParcel(Parcel in) {
            return new ResponseCreateBook(in);
        }

        @Override
        public ResponseCreateBook[] newArray(int size) {
            return new ResponseCreateBook[size];
        }
    };

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(postUrl);
        parcel.writeString(message);
        parcel.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
        parcel.writeParcelable(book, i);
    }
}
