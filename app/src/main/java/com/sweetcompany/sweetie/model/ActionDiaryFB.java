package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;
import com.sweetcompany.sweetie.calendar.ActionDiaryVM;

import java.util.Map;


public class ActionDiaryFB implements ActionDiaryVM {

    @Exclude
    private String key;
    private Map<String, MessageFB> messages;
    private int type;
    private String date;
    private String title;
    private String description;
    private String imageUri;

    public ActionDiaryFB() { }

    public ActionDiaryFB(int type, String date, String title, String description, String imageUri) {
        this.type = type;
        this.date = date;
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, MessageFB> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, MessageFB> messages) {
        this.messages = messages;
    }

    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
