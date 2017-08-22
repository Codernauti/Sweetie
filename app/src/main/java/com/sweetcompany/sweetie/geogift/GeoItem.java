package com.sweetcompany.sweetie.geogift;

/**
 * Created by ghiro on 16/08/2017.
 */

public class GeoItem {

    public final static int MESSAGE_GEOGIFT = 0;
    public final static int PHOTO_GEOGIFT = 1;
    public final static int HEART_GEOGIFT = 2;

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

    public GeoItem() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserCreatorUID() {
        return userCreatorUID;
    }

    public void setUserCreatorUID(String userCreatorUID) {
        this.userCreatorUID = userCreatorUID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
