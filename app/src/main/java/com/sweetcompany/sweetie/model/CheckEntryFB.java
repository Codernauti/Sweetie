package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;


public class CheckEntryFB {
    @Exclude
    private String key;

    private String userUid;   //TODO: add a user identifier
    private String text;
    private boolean checked;
    private String dateTime;

    public CheckEntryFB() {
    }

    public CheckEntryFB(String userUid, String text, boolean checked, String dateTime) {
        this.userUid = userUid;
        this.text = text;
        this.checked = checked;
        this.dateTime = dateTime;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
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
                " userUid: " + userUid +
                " text: " + text +
                " dateTime: " + dateTime +
                " checked: " + checked +
                "}";
    }
}
