package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;
import com.sweetcompany.sweetie.calendar.ActionDiaryVM;

import java.util.Map;

/**
 * Created by Eduard on 01-Aug-17.
 */

public class ActionDiaryFB implements ActionDiaryVM {

    @Exclude
    private String key;
    private Map<String, MessageFB> messages;
    private int type;
    private String date;

    public ActionDiaryFB() { }

    public ActionDiaryFB(int type, String date) {
        this.type = type;
        this.date = date;
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
}
