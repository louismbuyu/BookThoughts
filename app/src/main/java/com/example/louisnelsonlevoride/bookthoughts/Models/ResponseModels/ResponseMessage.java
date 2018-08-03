package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.louisnelsonlevoride.bookthoughts.Models.Message;

import java.util.List;

public class ResponseMessage implements Parcelable {
    Boolean success;
    String message;
    Message savedMessage;

    public ResponseMessage(Boolean success, String message, Message savedMessage) {
        this.success = success;
        this.message = message;
        this.savedMessage = savedMessage;
    }

    protected ResponseMessage(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
        message = in.readString();
        savedMessage = in.readParcelable(Message.class.getClassLoader());
    }

    public static final Creator<ResponseMessage> CREATOR = new Creator<ResponseMessage>() {
        @Override
        public ResponseMessage createFromParcel(Parcel in) {
            return new ResponseMessage(in);
        }

        @Override
        public ResponseMessage[] newArray(int size) {
            return new ResponseMessage[size];
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

    public Message getSavedMessage() {
        return savedMessage;
    }

    public void setSavedMessage(Message savedMessage) {
        this.savedMessage = savedMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
        parcel.writeString(message);
        parcel.writeParcelable(savedMessage, i);
    }
}
