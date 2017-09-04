package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by Eduard on 04-Sep-17.
 */

public class ActionNotification {

    @Exclude
    private String key;
    private int counter;

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
}
