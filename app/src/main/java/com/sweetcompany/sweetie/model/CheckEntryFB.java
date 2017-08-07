package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by lucas on 04/08/2017.
 */

public class CheckEntryFB {
    @Exclude
    private String key;

    private String email;   //TODO: add a user identifier
    private String text;
    private boolean checked;
    private String dateTime;

    public CheckEntryFB() {
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Exclude
    @Override
    public String toString() {
        return "{" +
                " key: " + key +
                " email: " + email +
                " text: " + text +
                " dateTime: " + dateTime +
                " checked: " + checked +
                "}";
    }
}
