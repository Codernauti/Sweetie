package com.sweetcompany.sweetie.Firebase;

import android.util.Log;

import com.google.firebase.database.Exclude;

/**
 * Created by Eduard on 21-May-17.
 */

public class Message {
    //TODO: add user fields
    @Exclude
    private String key;
    private String text;
    private String date;
    private boolean bookmarked;

    // For firebase serialization
    public Message() {}

    public Message(String text, String date, boolean bookmarked) {
        this.text = text;
        this.date = date;
        this.bookmarked = bookmarked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }


    @Exclude
    public String getTime() {
        // TODO: not reliable method; Hardcoded indexes
        if (date != null) {
            int indexStartHours = 11; //date.lastIndexOf(" " + 1);
            int indexEndMinutes = 16; //date.lastIndexOf(":");
            // take substring from char[12] to char[16]
            String time = date.substring(indexStartHours, indexEndMinutes);
            Log.d("DateTime Debug", "Time from substring: " + time);
            return time;
        }
        else return null;
    }

    @Exclude
    @Override
    public String toString() {
        return "{" +
                " key: " + key +
                " text: " + text +
                " date: " + date +
                " bookmarked: " + bookmarked +
                "}";
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    public String getKey() {
        return key;
    }
}
