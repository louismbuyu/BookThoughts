package com.example.louisnelsonlevoride.bookthoughts.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Chapter implements Parcelable {
    String _id;
    String title;
    int number;

    public Chapter(String _id, String title, int number) {
        this._id = _id;
        this.title = title;
        this.number = number;
    }

    protected Chapter(Parcel in) {
        _id = in.readString();
        title = in.readString();
        number = in.readInt();
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(title);
        parcel.writeInt(number);
    }
}
