package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.louisnelsonlevoride.bookthoughts.Models.RecentChat;

import java.util.List;

public class ResponseRecentChats implements Parcelable {
    Boolean success;
    List<RecentChat> recentChats;

    public ResponseRecentChats(Boolean success, List<RecentChat> recentChats) {
        this.success = success;
        this.recentChats = recentChats;
    }

    protected ResponseRecentChats(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
        recentChats = in.createTypedArrayList(RecentChat.CREATOR);
    }

    public static final Creator<ResponseRecentChats> CREATOR = new Creator<ResponseRecentChats>() {
        @Override
        public ResponseRecentChats createFromParcel(Parcel in) {
            return new ResponseRecentChats(in);
        }

        @Override
        public ResponseRecentChats[] newArray(int size) {
            return new ResponseRecentChats[size];
        }
    };

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<RecentChat> getRecentChats() {
        return recentChats;
    }

    public void setRecentChats(List<RecentChat> recentChats) {
        this.recentChats = recentChats;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
        parcel.writeTypedList(recentChats);
    }
}
