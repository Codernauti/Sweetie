package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by Eduard on 21-May-17.
 */

public class MessageFB {

    public final static int TEXT_MSG = 0;
    public final static int PHOTO_MSG = 1;

    @Exclude
    private String key;

    private String email;   //TODO: add a user identifier
    private String text;
    private String dateTime;
    private boolean bookmarked;
    private int type;
    private String uriStorage;

    private boolean uploading;

    // For firebase serialization
    public MessageFB() {}

    public MessageFB(String email, String text, String date, boolean bookmarked, int type, String uriS) {
        this.email = email;
        this.text = text;
        this.dateTime = date;
        this.bookmarked = bookmarked;
        this.type = type;
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
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }
    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public int getType(){
        return  this.type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public String getUriStorage() {
        return uriStorage;
    }
    public void setUriStorage(String uriS){
        this.uriStorage = uriS;
    }

    public boolean isUploading() {
        return uploading;
    }
    public void setUploading(boolean uploading) {
        this.uploading = uploading;
    }

    @Exclude
    public String getDate() {
        return dateTime.substring(0, 10);   // yyyy-mm-dd
    }
    @Exclude
    public String getYearAndMonth() {
        return dateTime.substring(0, 7);    // yyyy-mm
    }

    @Exclude
    public String getDay() {
        return dateTime.substring(8, 10);    // dd
    }

    @Exclude
    @Override
    public String toString() {
        return "{" +
                " key: " + key +
                " email: " + email +
                " text: " + text +
                " dateTime: " + dateTime +
                " bookmarked: " + bookmarked +
                "}";
    }
}
