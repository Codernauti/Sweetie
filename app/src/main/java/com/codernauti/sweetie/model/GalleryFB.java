package com.codernauti.sweetie.model;

import com.google.firebase.database.Exclude;

public class GalleryFB extends ChildActionFB {

    @Exclude
    private String key;

    private Double latitude;
    private Double longitude;
    private String address;
    private boolean imageSetByUser;


    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isImageSetByUser() {
        return imageSetByUser;
    }
    public void setImageSetByUser(boolean imageSetByUser) {
        this.imageSetByUser = imageSetByUser;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
