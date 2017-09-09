package com.sweetcompany.sweetie.map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ghiro on 24/08/2017.
 */

public class GalleryMapVM implements Parcelable{

    private String mKey;
    private String mTitle;
    private Double mLat;
    private Double mLon;
    private String mUriCover;

    GalleryMapVM(String key, Double lat, Double lon, String uriCover) {
        mKey = key;
        mLat = lat;
        mLon = lon;
        mUriCover = uriCover;
    }

    protected GalleryMapVM(Parcel in) {
        mKey = in.readString();
        mTitle = in.readString();
        mLat = in.readDouble();
        mLon = in.readDouble();
        mUriCover = in.readString();
    }

    public static final Creator<GalleryMapVM> CREATOR = new Creator<GalleryMapVM>() {
        @Override
        public GalleryMapVM createFromParcel(Parcel in) {
            return new GalleryMapVM(in);
        }

        @Override
        public GalleryMapVM[] newArray(int size) {
            return new GalleryMapVM[size];
        }
    };

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
    String getTitle() {
        return mTitle;
    }

    public Double getLat() {
        return mLat;
    }
    public void setLat(Double mLat) {
        this.mLat = mLat;
    }

    public Double getLon() {
        return mLon;
    }
    public void setLon(Double mLon) {
        this.mLon = mLon;
    }

    public String getUriCover() {
        return mUriCover;
    }
    public void setUriCover(String mUriCover) {
        this.mUriCover = mUriCover;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mKey);
        dest.writeString(mTitle);
        dest.writeDouble(mLat);
        dest.writeDouble(mLon);
        dest.writeString(mUriCover);
    }
}
