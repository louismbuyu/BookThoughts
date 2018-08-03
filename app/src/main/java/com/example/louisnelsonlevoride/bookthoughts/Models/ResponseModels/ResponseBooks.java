package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.louisnelsonlevoride.bookthoughts.Models.Book;

import java.util.List;

public class ResponseBooks implements Parcelable {
    private Boolean success;
    private List<Book> books;

    public ResponseBooks(Boolean success, List<Book> books) {
        this.success = success;
        this.books = books;
    }

    protected ResponseBooks(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
        books = in.createTypedArrayList(Book.CREATOR);
    }

    public static final Creator<ResponseBooks> CREATOR = new Creator<ResponseBooks>() {
        @Override
        public ResponseBooks createFromParcel(Parcel in) {
            return new ResponseBooks(in);
        }

        @Override
        public ResponseBooks[] newArray(int size) {
            return new ResponseBooks[size];
        }
    };

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
        parcel.writeTypedList(books);
    }
}
