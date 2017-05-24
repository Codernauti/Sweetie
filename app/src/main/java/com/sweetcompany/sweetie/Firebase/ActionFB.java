package com.sweetcompany.sweetie.Firebase;

import android.util.Log;

import com.google.firebase.database.Exclude;

/**
 * Created by ghiro on 18/05/2017.
 */

public class ActionFB {

    public final static int CHAT = 0;
    public final static int PHOTO = 1;

    private String mTitle;
    private String mLastUser;
    private String mDescription;
    private String mDdata;
    private int mType;
    private String date;

    ActionFB(){

    }

    public ActionFB(String title, String lastUser, String description, String date, int type) {
        this.mTitle = title;
        this.mLastUser = lastUser;
        this.mDescription = description;
        this.mDdata = date;
        this.mType = type;
    }


    /*** SETTER ***/

    public void setTitle(String title){
        this.mTitle = title;
    }

    public void setDescription(String description){
        this.mDescription = description;
    }

    public void setDataTime(String data){
        //setData e getData sono keyword già occupate
        this.mDdata = data;
    }

    /*** GETTER ***/ //TODO crate assert null function

    public String getTitle(){
        return mTitle;
    }

    public String getLastUser() { return mLastUser; }

    public String getDescription(){
        return mDescription;
    }

    public String getDataTime(){
        return mDdata;
    }

    public int getType() { return mType; }

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

}
