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

    GalleryMapVM(String key, String title) {
        mKey = key;
        mTitle = title;
    }

    String getTitle() {
        return mTitle;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmLon() {
        return mLon;
    }

    public void setmLon(String mLon) {
        this.mLon = mLon;
    }

    public String getmUriCover() {
        return mUriCover;
    }

    public void setmUriCover(String mUriCover) {
        this.mUriCover = mUriCover;
    }

}
