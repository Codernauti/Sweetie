package com.sweetcompany.sweetie.gallery;

/**
 * Created by ghiro on 22/07/2017.
 */

class GalleryVM {
    private String mKey;
    private String mTitle;
    private Double mLat;
    private Double mLon;
    private String mDate;
    private String mImageUri;

    GalleryVM(String key, String title) {
        mKey = key;
        mTitle = title;
    }

    GalleryVM(String key, String title, String date, String imageUri, Double lat, Double lon) {
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

    public String getImageUri() {
        return mImageUri;
    }

    public void setImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }
}
