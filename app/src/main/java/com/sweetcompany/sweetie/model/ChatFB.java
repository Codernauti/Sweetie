package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;
import com.sweetcompany.sweetie.actionInfo.ActionInfoVM;


public class ChatFB extends ChildActionFB {

    @Exclude
    private String key;

    /*private String title;
    private String uriCover;*/
    /*private String creationDate;*/

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

   /* public String getTitle() {
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
    }*/

    /*public String getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(String date) {
        this.creationDate = date;
    }*/
}
