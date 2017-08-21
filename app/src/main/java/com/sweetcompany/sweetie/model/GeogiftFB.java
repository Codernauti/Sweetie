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

    private String userCreatorUID;
    private int type;
    private String message;
    private String address;
    private String lat;
    private String lon;
    private String uriS;
    private boolean bookmarked;
    private String datetimeCreation;
    private String datetimeVisited;
    private boolean isVisited;


    // For firebase
    public GeogiftFB() {
    }


    @Exclude
    public String getKey() {
        return key;
    }

    public String getUserCreatorUID() {
        return userCreatorUID;
    }

    public void setUserCreatorUID(String userCreatorUID) {
        this.userCreatorUID = userCreatorUID;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public int getType() {
        return type;
    }

    public void setType(int mType) {
        this.type = mType;
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

    public String getDatetimeCreation() {
        return datetimeCreation;
    }

    public void setDatetimeCreation(String datetimeCreation) {
        this.datetimeCreation = datetimeCreation;
    }

    public String getDatetimeVisited() {
        return datetimeVisited;
    }

    public void setDatetimeVisited(String datetimeVisited) {
        this.datetimeVisited = datetimeVisited;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

}
