package com.example.louisnelsonlevoride.bookthoughts.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "book")
public class Book implements Parcelable {

    @PrimaryKey
    @NonNull String _id;
    String title;
    String author;
    String imageUrl;
    public String created_at;

    public Book(String _id, String title, String author, String imageUrl, String created_at) {
        this._id = _id;
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.created_at = created_at;
    }

    protected Book(Parcel in) {
        _id = in.readString();
        title = in.readString();
        author = in.readString();
        imageUrl = in.readString();
        created_at = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTimeStamp() {
        return created_at;
    }

    public void setTimeStamp(String timeStamp) {
        this.created_at = timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(imageUrl);
        parcel.writeString(created_at);
    }
}
