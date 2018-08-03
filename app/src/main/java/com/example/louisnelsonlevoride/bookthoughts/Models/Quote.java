package com.example.louisnelsonlevoride.bookthoughts.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Quote implements Parcelable {
    String _id;
    String text;
    String imageUrl;
    String thought;
    String updated_at;

    public Quote(String _id, String text, String imageUrl, String thought, String updated_at) {
        this._id = _id;
        this.text = text;
        this.imageUrl = imageUrl;
        this.thought = thought;
        this.updated_at = updated_at;
    }

    protected Quote(Parcel in) {
        _id = in.readString();
        text = in.readString();
        imageUrl = in.readString();
        thought = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
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

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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
        parcel.writeString(thought);
        parcel.writeString(updated_at);
    }
}
