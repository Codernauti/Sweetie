package com.sweetcompany.sweetie.map;

/**
 * Created by ghiro on 02/09/2017.
 */

public class GeogiftMapVM {


    private String mKey;

    private String mTitle;
    private Double mLat;
    private Double mLon;
    private int mType;
    private String mUriStorage;

    GeogiftMapVM(String key, Double lat, Double lon) {
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