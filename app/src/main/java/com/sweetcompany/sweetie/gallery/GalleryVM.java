package com.sweetcompany.sweetie.gallery;

/**
 * Created by ghiro on 22/07/2017.
 */

class GalleryVM {
    private String mKey;
    private String mTitle;
    private String mLat;
    private String mLon;
    private String mDate;
    private String mImageUri;

    GalleryVM(String key, String title) {
        mKey = key;
        mTitle = title;
    }

    GalleryVM(String key, String title, String date, String imageUri, String lat, String lon) {
        mKey = key;
        mTitle = title;
        mDate = date;
        mImageUri = imageUri;
        mLat = lat;
        mLon = lon;
    }

    String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
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

    public String getImageUri() {
        return mImageUri;
    }

    public void setImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }
}
