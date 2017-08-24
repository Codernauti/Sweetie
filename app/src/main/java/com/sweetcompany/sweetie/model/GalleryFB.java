package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by ghiro on 22/07/2017.
 */

public class GalleryFB {

    @Exclude
    private String key;

    private String title;
    private String latitude;
    private String longitude;
    private String uriCover;

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUriCover() {
        return uriCover;
    }

    public void setUriCover(String uriCover) {
        this.uriCover = uriCover;
    }
}
