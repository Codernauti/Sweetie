package com.sweetcompany.sweetie.Actions;

/**
 * Created by ghiro on 08/05/2017.
 */

public class ActionObj {

    private String mTitle;
    private String mDescription;
    private String mDdata; // TODO change format
    private String mType; // TODO string || int ?

    public ActionObj() {
    }

    public ActionObj(String title, String description, String date, String type) {
        // TODO assertion function true value
        this.mTitle = title;
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

    private void setType(String type){
        this.setType(type);
    }

    /*** GETTER ***/ //TODO crate assert null function

    public String getTitle(){
        return mTitle;
    }

    public String getDescription(){
        return mDescription;
    }

    public String getDataTime(){
        return mDdata;
    }

    public String getmType(){
        return mType;
    }
}
