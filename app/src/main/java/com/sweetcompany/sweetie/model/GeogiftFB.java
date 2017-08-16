package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by ghiro on 07/08/2017.
 */

public class GeogiftFB {

    public final static int MESSAGE_GEOGIFT = 0;
    public final static int PHOTO_GEOGIFT = 1;
    public final static int HEART_GEOGIFT = 2;

    @Exclude
    private String key;

    private String mEmail;
    private int mType;
    private String message;
    private String address;
    private String lat;
    private String lon;
    private String uriS;
    private boolean bookmarked;
    private String datetime;


    // For firebase
    public GeogiftFB() {
    }


    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUriS() {
        return uriS;
    }

    public void setUriS(String uriS) {
        this.uriS = uriS;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
