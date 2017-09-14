package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;


public class MsgNotification {

    @Exclude
    private String uid;
    private String chatUid;
    private String parentActionUid;
    private String chatTitle;
    private String text;


    // for firebase serialization
    public MsgNotification() { }

    public MsgNotification(String chatUid, String parentActionUid, String chatTitle) {
        this.chatUid = chatUid;
        this.parentActionUid = parentActionUid;
        this.chatTitle = chatTitle;
    }


    @Exclude
    public String getUid() {
        return uid;
    }

    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChatUid() {
        return chatUid;
    }

    public void setChatUid(String chatUid) {
        this.chatUid = chatUid;
    }

    public String getParentActionUid() {
        return parentActionUid;
    }

    public void setParentActionUid(String parentActionUid) {
        this.parentActionUid = parentActionUid;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
