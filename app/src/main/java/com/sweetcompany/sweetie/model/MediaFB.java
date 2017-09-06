package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by ghiro on 22/07/2017.
 */

public class MediaFB {
    @Exclude
    private String key;

    private String email;   //TODO: remove in future

    private String userUid;
    private String description;
    private String dateTime;
    private String uriStorage;
    private boolean bookmarked;

    private boolean uploading;
    private int progress;

    // For firebase serialization
    public MediaFB() {}

    public MediaFB(String email, String desc, String date, boolean bookmarked, String uriS, boolean uploading) {
        this.email = email;
        this.description = desc;
        this.dateTime = date;
        this.bookmarked = bookmarked;
        this.uriStorage = uriS;
        this.uploading = uploading;
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

    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getUserUid() {
        return userUid;
    }
    public void setUserUid(String userUid) {
        this.userUid = userUid;
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
