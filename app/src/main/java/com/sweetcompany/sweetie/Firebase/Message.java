package com.sweetcompany.sweetie.Firebase;

import android.util.Log;

import com.google.firebase.database.Exclude;

/**
 * Created by Eduard on 21-May-17.
 */

public class Message {
    //TODO: add user fields
    private String text;
    private String date;

    // For firebase serialization
    public Message() {}

    public Message(String text, String date) {
        this.text = text;
        this.date = date;
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

    @Exclude
    public String getTime() {
        if (date != null) {
            int indexSpace = date.lastIndexOf(" " + 1);
            int indexLastDots = date.lastIndexOf(":");
            String time = date.substring(indexSpace, indexLastDots);
            Log.d("DateTime Debug", "Time from substring: " + time);
            return time;
        }
        else return null;
    }
}
