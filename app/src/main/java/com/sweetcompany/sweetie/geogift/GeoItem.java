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
    private String title;
    private String message;
    private String address;
    private Double lat;
    private Double lon;
    private String uriS;
    private boolean bookmarked;
    private String datetimeCreation;
    private String datetimeVisited;
    private boolean isTriggered;

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

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
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

    public boolean getIsTriggered() {
        return isTriggered;
    }

    public void setIsTriggered(boolean triggered) {
        this.isTriggered = triggered;
    }

}
