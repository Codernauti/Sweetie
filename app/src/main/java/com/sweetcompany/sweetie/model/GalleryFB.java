package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by ghiro on 22/07/2017.
 */

public class GalleryFB extends ChildActionFB {

    @Exclude
    private String key;

    private Double latitude;
    private Double longitude;
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
}
