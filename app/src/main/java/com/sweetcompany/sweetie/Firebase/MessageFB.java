package com.sweetcompany.sweetie.Firebase;

import android.util.Log;

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

    // TODO: for firebase, remove in future
    public String getDate() { return dateTime; }
    public void setDate(String date) { this.dateTime = date; }

    public boolean isBookmarked() {
        return bookmarked;
    }
    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    @Exclude
    public String getTime() {
        // TODO: not reliable method; Hardcoded indexes
        if (dateTime != null) {
            int indexStartHours = 11; //dateTime.lastIndexOf(" " + 1);
            int indexEndMinutes = 16; //dateTime.lastIndexOf(":");
            // take substring from char[12] to char[16]
            String time = dateTime.substring(indexStartHours, indexEndMinutes);
            Log.d("DateTime Debug", "Time from substring: " + time);
            return time;
        }
        else return "no time";
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
