package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by Eduard on 21-May-17.
 */

public class MessageFB {
    @Exclude
    private String key;

    private String email;   //TODO: add a user identifier
    private String text;
    private String dateTime;
    private boolean bookmarked;

    // For firebase serialization
    public MessageFB() {}

    public MessageFB(String email, String text, String date, boolean bookmarked) {
        this.email = email;
        this.text = text;
        this.dateTime = date;
        this.bookmarked = bookmarked;
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
