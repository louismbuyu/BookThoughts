package com.example.louisnelsonlevoride.bookthoughts.Models.ResponseModels;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.louisnelsonlevoride.bookthoughts.Models.Chapter;

import java.util.List;

public class ResponseChapters implements Parcelable {
    private Boolean success;
    private List<Chapter> chapters;

    public ResponseChapters(Boolean success, List<Chapter> chapters) {
        this.success = success;
        this.chapters = chapters;
    }

    protected ResponseChapters(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
    }

    public static final Creator<ResponseChapters> CREATOR = new Creator<ResponseChapters>() {
        @Override
        public ResponseChapters createFromParcel(Parcel in) {
            return new ResponseChapters(in);
        }

        @Override
        public ResponseChapters[] newArray(int size) {
            return new ResponseChapters[size];
        }
    };

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
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
