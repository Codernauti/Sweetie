package com.codernauti.sweetie.model;

import com.google.firebase.database.Exclude;


public class MediaFB {
    @Exclude
    private String key;

    private String userUid;
    private String description;
    private String dateTime;
    private String uriStorage;
    private boolean bookmarked;

    private boolean uploading;
    private int progress;

    // For firebase serialization
    public MediaFB() {}

    public MediaFB(String userUid, String desc, String date, boolean bookmarked, String uriS, boolean uploading) {
        this.userUid = userUid;
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

    public String getUserUid() { return userUid; }
    public void setUserUid(String userUid) { this.userUid = userUid; }

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

    @Exclude
    @Override
    public String toString() {
        return "{" +
                " key: " + key +
                " userUid: " + userUid +
                " text: " + description +
                " dateTime: " + dateTime +
                "}";
    }
}
