package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;
import com.sweetcompany.sweetie.actionInfo.ActionInfoVM;

/**
 * Created by Eduard on 25-May-17.
 */

public class ChatFB implements ActionInfoVM {

    @Exclude
    private String key;

    private String title;
    private String uriCover;
    private String date;

    // For firebase
    public ChatFB() {}

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUriCover() {
        return uriCover;
    }

    public void setUriCover(String uriCover) {
        this.uriCover = uriCover;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
