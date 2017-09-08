package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

import java.util.Map;

/**
 * Created by Eduard on 04-Sep-17.
 */

public class ActionNotification {

    @Exclude
    private String key;
    private int counter;
    private Map<String, Boolean> updatedElements;

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Map<String, Boolean> getUpdatedElements() {
        return updatedElements;
    }

    public void setUpdatedElements(Map<String, Boolean> updatedElements) {
        this.updatedElements = updatedElements;
    }
}
