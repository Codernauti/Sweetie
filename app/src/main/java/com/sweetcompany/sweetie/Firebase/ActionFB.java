package com.sweetcompany.sweetie.Firebase;

import android.util.Log;

import com.google.firebase.database.Exclude;

/**
 * Created by ghiro on 18/05/2017.
 */

public class ActionFB {

    public final static int CHAT = 0;
    public final static int PHOTO = 1;

    private String title;
    private String lastUser;
    private String description;
    private String data;
    private int type;
    private String date;
    private String childKey;

    ActionFB(){

    }

    public ActionFB(String title, String lastUser, String description, String date, int type) {
        this.title = title;
        this.lastUser = lastUser;
        this.description = description;
        this.data = date;
        this.type = type;
    }


    /*** SETTER ***/

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setDataTime(String data){
        //setData e getData sono keyword già occupate
        this.data = data;
    }

    public void setType(int type){
        this.type = type;
    }

    /*** GETTER ***/ //TODO crate assert null function

    public String getTitle(){
        return title;
    }

    public String getLastUser() { return lastUser; }

    public String getDescription(){
        return description;
    }

    public String getDataTime(){
        return data;
    }

    public int getType() { return type; }

    @Exclude
    public String getTime() {
        if (date != null) {
            int indexSpace = date.lastIndexOf(" " + 1);
            int indexLastDots = date.lastIndexOf(":");
            String time = date.substring(indexSpace, indexLastDots);
            Log.d("DateTime Debug", "Time from substring: " + time);
            return time;
        }
        else return null;
    }


    public String getChildKey() {
        return this.childKey;
    }

    public void setChildKey(String childKey) {
        this.childKey = childKey;
    }
}
