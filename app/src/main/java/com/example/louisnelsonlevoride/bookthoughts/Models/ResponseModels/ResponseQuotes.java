package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.louisnelsonlevoride.bookthoughts.Models.Quote;

import java.util.List;

public class ResponseQuotes implements Parcelable {
    private Boolean success;
    private List<Quote> quotes;

    public ResponseQuotes(Boolean success, List<Quote> quotes) {
        this.success = success;
        this.quotes = quotes;
    }

    protected ResponseQuotes(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResponseQuotes> CREATOR = new Creator<ResponseQuotes>() {
        @Override
        public ResponseQuotes createFromParcel(Parcel in) {
            return new ResponseQuotes(in);
        }

        @Override
        public ResponseQuotes[] newArray(int size) {
            return new ResponseQuotes[size];
        }
    };

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }
}
