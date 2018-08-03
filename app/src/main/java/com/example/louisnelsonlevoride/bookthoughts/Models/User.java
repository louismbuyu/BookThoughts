package com.example.louisnelsonlevoride.bookthoughts.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String username;
    private String _id;
    private String displayName;
    private String imageUrl;

    public User(String username, String _id, String displayName,String imageUrl) {
        this.username = username;
        this._id = _id;
        this.displayName = displayName;
        this.imageUrl = imageUrl;
    }

    protected User(Parcel in) {
        username = in.readString();
        _id = in.readString();
        displayName = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return _id;
    }

    public void setUserId(String userId) {
        this._id = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(_id);
        parcel.writeString(displayName);
        parcel.writeString(imageUrl);
    }
}
