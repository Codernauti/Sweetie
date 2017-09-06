package com.sweetcompany.sweetie.gallery;

import android.view.View;

/**
 * Created by ghiro on 25/07/2017.
 */

abstract class MediaVM implements GalleryItemVM{
    static final boolean THE_MAIN_USER = true;
    static final boolean THE_PARTNER = false;

    private String mKey;

    private final boolean mWho;
    private String mTime;           // Format HH:mm
    private String mDescription;
    private String mUriStorage;
    private int mPercent;

    MediaVM(boolean who, String date, String desc, String uriS, int perc,  String key) {
        mWho = who;
        mTime = date;
        mDescription = desc;
        mUriStorage = uriS;
        mPercent = perc;

        mKey = key;
    }

    boolean isTheMainUser() {
        return mWho;
    }

    String getTime() {
        return mTime;
    }
    void setTime(String time){
        mTime = time;
    }

    String getDescription(){ return mDescription;}
    void setDescription(String desc){ mDescription = desc;}

    @Override
    public String getKey() { return mKey; }
    void setKey(String key){
        this.mKey = key;
    }

    String getUriStorage(){
        return mUriStorage;
    }
    void setUriStorage(String uriS){
        mUriStorage = uriS;
    }

    int getPercent(){
        return mPercent;
    }
    void setPercent(int perc){
        mPercent = perc;
    }

    abstract void configViewHolder(MediaViewHolder viewHolder);

}
