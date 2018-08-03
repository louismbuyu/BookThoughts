package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.louisnelsonlevoride.bookthoughts.Models.Message;

import java.util.List;

public class ResponseMessages implements Parcelable {
    Boolean success;
    String message;
    List<Message> messages;

    public ResponseMessages(Boolean success, String message, List<Message> messages) {
        this.success = success;
        this.message = message;
        this.messages = messages;
    }

    protected ResponseMessages(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
        message = in.readString();
        messages = in.createTypedArrayList(Message.CREATOR);
    }

    public static final Creator<ResponseMessages> CREATOR = new Creator<ResponseMessages>() {
        @Override
        public ResponseMessages createFromParcel(Parcel in) {
            return new ResponseMessages(in);
        }

        @Override
        public ResponseMessages[] newArray(int size) {
            return new ResponseMessages[size];
        }
    };

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
        parcel.writeString(message);
        parcel.writeTypedList(messages);
    }
}
