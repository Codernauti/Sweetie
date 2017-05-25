package com.sweetcompany.sweetie.Firebase;

import com.google.firebase.database.Exclude;

/**
 * Created by Eduard on 25-May-17.
 */

public class ChatFB {

    @Exclude
    private String key;

    private String title;


    // For firebase
    public ChatFB() {}


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


}
