package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by lucas on 04/08/2017.
 */

public class ToDoListFB {

    @Exclude
    private String key;

    private String title;

    public ToDoListFB() {
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
