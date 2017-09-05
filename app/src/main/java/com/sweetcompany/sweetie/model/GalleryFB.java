package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by ghiro on 22/07/2017.
 */

public class GalleryFB {

    @Exclude
    private String key;

    private String title;
    private String creationDate;
    private Double latitude;
    private Double longitude;
    private String uriCover;
    private boolean imageSetByUser;

    // For firebase
    public GalleryFB() {}

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(String date) {
        this.creationDate = date;
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

    public String getUriCover() {
        return uriCover;
    }
    public void setUriCover(String uriCover) {
        this.uriCover = uriCover;
    }

    public boolean isImageSetByUser() {
        return imageSetByUser;
    }
    public void setImageSetByUser(boolean imageSetByUser) {
        this.imageSetByUser = imageSetByUser;
    }
}
