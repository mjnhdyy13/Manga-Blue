package com.hfad.readapp2.searchview;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.io.Serializable;

@SuppressLint("ParcelCreator")
public class Suggestion implements SearchSuggestion, Serializable {
    private String mName,mlink,anh;
    private boolean mIsHistory = false;

    public Suggestion(String mName,String mlink,String anh) {
        this.mName = mName.toLowerCase();
        this.mlink = mlink;
        this.anh = anh;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    public String getMlink() {
        return mlink;
    }

    public void setMlink(String mlink) {
        this.mlink = mlink;
    }

    @Override
    public String getBody() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

}