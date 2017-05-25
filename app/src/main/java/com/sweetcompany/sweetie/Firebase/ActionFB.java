package com.sweetcompany.sweetie.Firebase;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ghiro on 18/05/2017.
 */

public class ActionFB {

    public final static int CHAT = 0;
    public final static int PHOTO = 1;

    private String title;
    private String lastUser;
    private String description;
    private String dateTime;
    private int type;
    private String childKey;

    ActionFB() {}

    public ActionFB(String title, String lastUser, String description, String date, int type) {
        this.title = title;
        this.lastUser = lastUser;
        this.description = description;
        this.dateTime = date;
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
        this.dateTime = data;
    }

    public void setType(int type){
        this.type = type;
    }

    public void setChildKey(String childKey) {
        this.childKey = childKey;
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
        return dateTime;
    }

    public int getType() { return type; }

    public String getChildKey() {
        return this.childKey;
    }

    @Exclude
    public String getTime() {
        if (dateTime != null) {
            // TODO: hardcoded indexes!
            /*int indexSpace = 11;
            int indexLastDots = 16;
            String time = dateTime.substring(indexSpace, indexLastDots);*/

            try {
                // get Date from dateTime (standard format)
                DateFormat standardFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                Date date = standardFormat.parse(dateTime);

                // TODO: choice date based from local device settings
                DateFormat viewFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);

                // convert Date to the view format to show to user
                String viewFormatString = viewFormat.format(date);

                Log.d("DateTime Debug", "Time from substring: " + viewFormatString);
                return viewFormatString;

            } catch (ParseException e) {
                e.printStackTrace();
                return "no time";
            }
        }
        else return "no time";
    }
}
