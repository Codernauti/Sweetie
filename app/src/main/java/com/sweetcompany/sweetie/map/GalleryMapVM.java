package com.sweetcompany.sweetie.map;

/**
 * Created by ghiro on 24/08/2017.
 */

public class GalleryMapVM {

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


}
