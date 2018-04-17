package com.codernauti.sweetie.gallery;

class GalleryVM {
    private String mKey;
    private String mTitle;
    private Double mLat;
    private Double mLon;
    private String mCreationDate;
    private String mImageUri;

    GalleryVM(String key, String title) {
        mKey = key;
        mTitle = title;
    }

    GalleryVM(String key, String title, String creationDate, String imageUri, Double lat, Double lon) {
        mKey = key;
        mTitle = title;
        mCreationDate = creationDate;
        mImageUri = imageUri;
        mLat = lat;
        mLon = lon;
    }

    String getTitle() {
        return mTitle;
    }

    public String getCreationDate() {
        return mCreationDate;
    }
    public void setCreationDate(String mCreationDate) {
        this.mCreationDate = mCreationDate;
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
