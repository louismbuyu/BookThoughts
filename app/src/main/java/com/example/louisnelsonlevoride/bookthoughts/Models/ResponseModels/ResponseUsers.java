package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.louisnelsonlevoride.bookthoughts.Models.User;

import java.util.List;

public class ResponseUsers implements Parcelable {
    private Boolean success;
    private List<User> users;
    private String error;

    public ResponseUsers(Boolean success, List<User> users, String error) {
        this.success = success;
        this.users = users;
        this.error = error;
    }

    protected ResponseUsers(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
        users = in.createTypedArrayList(User.CREATOR);
        error = in.readString();
    }

    public static final Creator<ResponseUsers> CREATOR = new Creator<ResponseUsers>() {
        @Override
        public ResponseUsers createFromParcel(Parcel in) {
            return new ResponseUsers(in);
        }

        @Override
        public ResponseUsers[] newArray(int size) {
            return new ResponseUsers[size];
        }
    };

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
        parcel.writeTypedList(users);
        parcel.writeString(error);
    }
}
