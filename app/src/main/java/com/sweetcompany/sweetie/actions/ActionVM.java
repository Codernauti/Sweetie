package com.sweetcompany.sweetie.actions;

import android.content.Context;

import com.sweetcompany.sweetie.model.ActionFB;

/**
 * Created by ghiro on 08/05/2017.
 */

// TODO abstract class
abstract class ActionVM {

    static final int CHAT = ActionFB.CHAT;
    static final int GALLERY = ActionFB.GALLERY;
    static final int GEOGIFT = ActionFB.GEOGIFT;
    static final int TODOLIST= ActionFB.TODOLIST;

    protected Context mContext;

    private String mKeyFB; //FB actions references
    private String mTitle;
    private String mLastUser;
    private String mDescription;
    private String mDataTime; // TODO change format
    private int mType;
    private String mChildUid;

    ActionVM() {
    }

    ActionVM(String key, String title, String lastUser, String description, String date, int type, String childKey) {
        // TODO assertion function true value
        mKeyFB = key;
        mTitle = title;
        mLastUser = lastUser;
        mDescription = description;
        mDataTime = date;
        mType = type;
        mChildUid = childKey;
    }

    void setContext(Context context){
        mContext = context;
    }


    String getKey(){
        return mKeyFB;
    }

    void setKey(String key){
        this.mKeyFB = key;
    }

    String getTitle(){
        return mTitle;
    }

    void setTitle(String title){
        this.mTitle = title;
    }

    String getDescription(){
        return mDescription;
    }

    void setDescription(String description){
        this.mDescription = description;
    }

    String getDataTime(){
        return mDataTime;
    }

    void setDataTime(String data){
        this.mDataTime = data;
    }

    int getType() { return mType; }

    void setType(int type){
        this.mType = type;
    }

    String getChildUid() { return mChildUid; }

    void setChildUid(String childUid) {
        this.mChildUid = childUid;
    }


    /*** abstract methods ***/

    public abstract int getChildType();

    public abstract void showAction();

    public abstract int getIconId();

}
