package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.louisnelsonlevoride.bookthoughts.Models.User;

public class ResponseComMessage implements Parcelable {

    String _id;
    String text;
    String imageUrl;
    String chatRoomId;
    User senderUser;
    User receiverUser;
    String created_at;
    Boolean read;

    public ResponseComMessage(String _id, String text, String imageUrl, String chatRoomId, User senderUser, User receiverUser, String created_at, Boolean read) {
        this._id = _id;
        this.text = text;
        this.imageUrl = imageUrl;
        this.chatRoomId = chatRoomId;
        this.senderUser = senderUser;
        this.receiverUser = receiverUser;
        this.created_at = created_at;
        this.read = read;
    }

    protected ResponseComMessage(Parcel in) {
        _id = in.readString();
        text = in.readString();
        imageUrl = in.readString();
        chatRoomId = in.readString();
        senderUser = in.readParcelable(User.class.getClassLoader());
        receiverUser = in.readParcelable(User.class.getClassLoader());
        created_at = in.readString();
        byte tmpRead = in.readByte();
        read = tmpRead == 0 ? null : tmpRead == 1;
    }

    public static final Creator<ResponseComMessage> CREATOR = new Creator<ResponseComMessage>() {
        @Override
        public ResponseComMessage createFromParcel(Parcel in) {
            return new ResponseComMessage(in);
        }

        @Override
        public ResponseComMessage[] newArray(int size) {
            return new ResponseComMessage[size];
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

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public User getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(User receiverUser) {
        this.receiverUser = receiverUser;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
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
        parcel.writeString(chatRoomId);
        parcel.writeParcelable(senderUser, i);
        parcel.writeParcelable(receiverUser, i);
        parcel.writeString(created_at);
        parcel.writeByte((byte) (read == null ? 0 : read ? 1 : 2));
    }
}
