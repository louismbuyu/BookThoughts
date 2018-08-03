package com.example.louisnelsonlevoride.bookthoughts.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class RecentChat implements Parcelable {
    String _id;
    String text;
    String imageUrl;
    String timestamp;
    Boolean read;
    User senderUser;
    User receiverUser;

    public RecentChat(String _id, String text, String imageUrl, String timestamp, Boolean read, User sender, User receiver) {
        this._id = _id;
        this.text = text;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.read = read;
        this.senderUser = sender;
        this.receiverUser = receiver;
    }

    protected RecentChat(Parcel in) {
        _id = in.readString();
        text = in.readString();
        imageUrl = in.readString();
        timestamp = in.readString();
        byte tmpRead = in.readByte();
        read = tmpRead == 0 ? null : tmpRead == 1;
        senderUser = in.readParcelable(User.class.getClassLoader());
        receiverUser = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<RecentChat> CREATOR = new Creator<RecentChat>() {
        @Override
        public RecentChat createFromParcel(Parcel in) {
            return new RecentChat(in);
        }

        @Override
        public RecentChat[] newArray(int size) {
            return new RecentChat[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public User getSender() {
        return senderUser;
    }

    public void setSender(User sender) {
        this.senderUser = sender;
    }

    public User getReceiver() {
        return receiverUser;
    }

    public void setReceiver(User receiver) {
        this.receiverUser = receiver;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(text);
        parcel.writeString(imageUrl);
        parcel.writeString(timestamp);
        parcel.writeByte((byte) (read == null ? 0 : read ? 1 : 2));
        parcel.writeParcelable(senderUser, i);
        parcel.writeParcelable(receiverUser, i);
    }
}
