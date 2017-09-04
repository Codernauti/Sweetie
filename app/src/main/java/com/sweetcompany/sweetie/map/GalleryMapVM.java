package com.sweetcompany.sweetie.map;

/**
 * Created by ghiro on 24/08/2017.
 */

public class GalleryMapVM {

    private String mKey;
    private String mTitle;
    private String mLat;
    private String mLon;
    private String mUriCover;

    GalleryMapVM(String key, String lat, String lon, String uriCover) {
        mKey = key;
        mLat = lat;
        mLon = lon;
        mUriCover = uriCover;
    }

    String getTitle() {
        return mTitle;
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

    public String getUriCover() {
        return mUriCover;
    }

    public void setUriCover(String mUriCover) {
        this.mUriCover = mUriCover;
    }

}
