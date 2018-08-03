package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

public class ResponseCreate implements Parcelable {
    Boolean success;

    public ResponseCreate(Boolean success) {
        this.success = success;
    }

    protected ResponseCreate(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
    }

    public static final Creator<ResponseCreate> CREATOR = new Creator<ResponseCreate>() {
        @Override
        public ResponseCreate createFromParcel(Parcel in) {
            return new ResponseCreate(in);
        }

        @Override
        public ResponseCreate[] newArray(int size) {
            return new ResponseCreate[size];
        }
    };

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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
