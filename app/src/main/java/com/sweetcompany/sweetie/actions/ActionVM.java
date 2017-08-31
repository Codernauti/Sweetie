package com.sweetcompany.sweetie.actions;

import android.content.Context;

import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.text.ParseException;

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
    private String mImageUrl;

    ActionVM() {
    }

    ActionVM(String key, String title, String lastUser, String description, String date, int type,
             String childKey, String imageUrl) {

        mKeyFB = key;
        mTitle = title;
        mLastUser = lastUser;
        mDescription = description;
        mDataTime = date;
        mType = type;
        mChildUid = childKey;
        mImageUrl = imageUrl;
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

    String getImageUrl() {
        return mImageUrl;
    }

    void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    /*** abstract methods ***/

    public abstract int getChildType();

    public abstract void showAction();

    public abstract int getIconId();

    // TODO: think about make abstract this method
    public void configViewHolder(ActionsAdapter.ActionViewHolder viewHolder) {
        viewHolder.setTitle(mTitle);
        viewHolder.setDescription(mDescription);
        viewHolder.setAvatar(mImageUrl);
        viewHolder.setTypeIcon(getIconId());

        try {
            viewHolder.setDateTime(DataMaker.get_Date_4_Action(mDataTime));
        } catch (ParseException e) {
            viewHolder.setDateTime("error");
            e.printStackTrace();
        }
    }
}
