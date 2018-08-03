package com.example.louisnelsonlevoride.bookthoughts.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {

    String _id;
    String text;
    String imageUrl;
    String chatRoomId;
    User senderUser;
    User receiverUser;
    String senderId;
    String receiverId;
    String created_at;
    Boolean read;

    public Message(String _id, String text, String imageUrl, String chatRoomId, User senderUser, User receiverUser, String senderId, String receiverId, String created_at, Boolean read) {
        this._id = _id;
        this.text = text;
        this.imageUrl = imageUrl;
        this.chatRoomId = chatRoomId;
        this.senderUser = senderUser;
        this.receiverUser = receiverUser;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.created_at = created_at;
        this.read = read;
    }

    protected Message(Parcel in) {
        _id = in.readString();
        text = in.readString();
        imageUrl = in.readString();
        chatRoomId = in.readString();
        senderUser = in.readParcelable(User.class.getClassLoader());
        receiverUser = in.readParcelable(User.class.getClassLoader());
        senderId = in.readString();
        receiverId = in.readString();
        created_at = in.readString();
        byte tmpRead = in.readByte();
        read = tmpRead == 0 ? null : tmpRead == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(text);
        dest.writeString(imageUrl);
        dest.writeString(chatRoomId);
        dest.writeParcelable(senderUser, flags);
        dest.writeParcelable(receiverUser, flags);
        dest.writeString(senderId);
        dest.writeString(receiverId);
        dest.writeString(created_at);
        dest.writeByte((byte) (read == null ? 0 : read ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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
}
