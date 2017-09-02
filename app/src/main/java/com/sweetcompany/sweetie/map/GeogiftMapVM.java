package com.sweetcompany.sweetie.map;

/**
 * Created by ghiro on 02/09/2017.
 */

public class GeogiftMapVM {


    private String mKey;

    private String mTitle;
    private String mLat;
    private String mLon;
    private int mType;
    private String mUriStorage;

    GeogiftMapVM(String key, String lat, String lon) {
        mKey = key;
        mLat = lat;
        mLon = lon;
    }

    String getTitle() {
        return mTitle;
    }
    public void setTitle(String title){
        this.mTitle = title;
    }

    public String getLat() {
        return mLat;
    }
    public void setLat(String mLat) {
        this.mLat = mLat;
    }

    public String getLon() {
        return mLon;
    }
    public void setLon(String mLon) {
        this.mLon = mLon;
    }

    public int getType() {
        return mType;
    }
    public void setType(int mType) {
        this.mType = mType;
    }

    public String getUriStorage() {
        return mUriStorage;
    }
    public void setUriStorage(String mUriStorage) {
        this.mUriStorage = mUriStorage;
    }
}