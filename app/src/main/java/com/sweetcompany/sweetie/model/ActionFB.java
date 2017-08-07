package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;

/**
 * Created by ghiro on 18/05/2017.
 */

public class ActionFB {

    public final static int CHAT = 0;
    public final static int GALLERY = 1;
    public final static int TODOLIST = 2;

    private String key;
    private String title;
    private String lastUser;
    private String description;
    private String dateTime;
    private int type;
    private String childKey;
    private String actionKey;

    ActionFB() {}

    public ActionFB(String title, String lastUser, String description, String date, int type) {
        this.title = title;
        this.lastUser = lastUser;
        this.description = description;
        this.dateTime = date;
        this.type = type;
    }


    /*** SETTER ***/
    @Exclude
    public void setKey(String key){
        this.key = key;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setDataTime(String data){
        this.dateTime = data;
    }

    public void setType(int type){
        this.type = type;
    }

    public void setChildKey(String childKey) {
        this.childKey = childKey;
    }

    public void setActionKey(String childKey) {
        this.actionKey = actionKey;
    }

    /*** GETTER ***/ //TODO crate assert null function
    @Exclude
    public String getKey(){
        return key;
    }

    public String getTitle(){
        return title;
    }

    public String getLastUser() { return lastUser; }

    public String getDescription(){
        return description;
    }

    public String getDataTime(){
        return dateTime;
    }

    public int getType() { return type; }

    public String getChildKey() {
        return this.childKey;
    }

}
