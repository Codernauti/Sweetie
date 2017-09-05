package com.sweetcompany.sweetie.actions;

import android.content.Context;
import android.support.v4.content.ContextCompat;

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
    private String mLastUpdateDate;
    private int mType;
    private String mChildUid;
    private String mImageUrl;
    private int mNotificCounter;

    ActionVM() {
    }

    ActionVM(String key, String title, String lastUser, String description, String lastUpdateDate, int type,
             String childKey, String imageUrl, int notificCounter) {

        mKeyFB = key;
        mTitle = title;
        mLastUser = lastUser;
        mDescription = description;
        mLastUpdateDate = lastUpdateDate;
        mType = type;
        mChildUid = childKey;
        mImageUrl = imageUrl;
        mNotificCounter = notificCounter;
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

    String getLastUpdateDate(){
        return mLastUpdateDate;
    }

    void setLastUpdateDate(String data){
        this.mLastUpdateDate = data;
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

    int getNotificationCounter() { return mNotificCounter; }
    void setNotificationCounter(int notificationCounter) { mNotificCounter = notificationCounter; }

    /*** abstract methods ***/

    public abstract int getChildType();

    public abstract void showAction();

    public abstract int getIconId();

    public abstract int getAvatarTextIdColor();

    // TODO: think about make abstract this method
    public void configViewHolder(ActionsAdapter.ActionViewHolder viewHolder) {
        viewHolder.setTitle(mTitle);
        viewHolder.setDescription(mDescription);
        viewHolder.setAvatar(mImageUrl, getAvatarTextIdColor());
        viewHolder.setTypeIcon(getIconId());
        viewHolder.setNotificationCount(mNotificCounter, ContextCompat.getColor(mContext, getAvatarTextIdColor()));

        try {
            viewHolder.setDateTime(DataMaker.get_Date_4_Action(mLastUpdateDate));
        } catch (ParseException e) {
            viewHolder.setDateTime("error");
            e.printStackTrace();
        }
    }
}
