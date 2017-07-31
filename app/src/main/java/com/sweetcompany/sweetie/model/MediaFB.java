package com.sweetcompany.sweetie.model;

import android.net.Uri;

import com.google.firebase.database.Exclude;

/**
 * Created by ghiro on 22/07/2017.
 */

public class MediaFB {
    @Exclude
    private String key;

    private String email;   //TODO: add a user identifier
    private String description;
    private String dateTime;
    //private String encode;
    private String uriLocal;
    private String uriStorage;
    private boolean bookmarked;

    // For firebase serialization
    public MediaFB() {}

    public MediaFB(String email, String desc, String date, boolean bookmarked, String uriL, String uriS) {
        this.email = email;
        this.description = desc;
        this.dateTime = date;
        this.bookmarked = bookmarked;
        //this.encode = encode;
        this.uriLocal = uriL;
        this.uriStorage = uriS;
    }


    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getText() {
        return description;
    }
    public void setText(String desc) {
        this.description = desc;
    }

    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    // TODO: for firebase, remove in future
    public String getDate() { return dateTime; }
    public void setDate(String date) { this.dateTime = date; }

    public boolean isBookmarked() {
        return bookmarked;
    }
    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public String getUriLocal() {
        return uriLocal;
    }
    public void setUriLocal(String uriL){
        this.uriLocal = uriL;
    }

    public String getUriStorage() {
        return uriStorage;
    }
    public void setUriStorage(String uriS){
        this.uriStorage = uriS;
    }

    @Exclude
    @Override
    public String toString() {
        return "{" +
                " key: " + key +
                " email: " + email +
                " text: " + description +
                " dateTime: " + dateTime +
                "}";
    }
}
