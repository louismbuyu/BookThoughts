package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.louisnelsonlevoride.bookthoughts.Models.User;

public class ResponseCreateUser implements Parcelable {
    private Boolean success;
    private User user;

    public ResponseCreateUser(Boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    protected ResponseCreateUser(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
    }

    public static final Creator<ResponseCreateUser> CREATOR = new Creator<ResponseCreateUser>() {
        @Override
        public ResponseCreateUser createFromParcel(Parcel in) {
            return new ResponseCreateUser(in);
        }

        @Override
        public ResponseCreateUser[] newArray(int size) {
            return new ResponseCreateUser[size];
        }
    };

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
    }
}
