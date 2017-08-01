package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

import java.util.Map;

/**
 * Created by Eduard on 01-Aug-17.
 */

public class ActionDiaryFB {

    @Exclude
    private String key;
    private Map<String, MessageFB> messages;


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
}
