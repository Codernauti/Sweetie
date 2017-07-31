package com.sweetcompany.sweetie.gallery;

import android.view.View;

/**
 * Created by ghiro on 25/07/2017.
 */

abstract class MediaVM {
    static final boolean THE_MAIN_USER = true;
    static final boolean THE_PARTNER = false;

    private final String mKey;

    private final boolean mWho;
    private String mTime;
    private String description;// Format HH:mm
    private String uriLocal;
    private String uriStorage;
    private int percent;

    MediaVM(boolean who, String date, String desc, String key, String uriL, String uriS, int perc) {
        mWho = who;
        mTime = date;
        description = desc;
        mKey = key;
        uriLocal = uriL;
        uriStorage = uriS;
        percent = perc;
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

    String getDescription(){ return description;}
    void setDescription(String desc){ description = desc;}

    String getKey() { return mKey; }

    String getUriLocal(){
        return uriLocal;
    }
    void setUriLocal(String uriL){
        uriLocal = uriL;
    }

    String getUriStorage(){
        return uriStorage;
    }
    void setUriStorage(String uriS){
        uriStorage = uriS;
    }

    int getPercent(){
        return percent;
    }
    void setPercent(int perc){
        percent = perc;
    }

    abstract void configViewHolder(MediaViewHolder viewHolder);

    abstract int getIdView();

    @Deprecated
    abstract MediaViewHolder newViewHolder(View inflatedView);

}
