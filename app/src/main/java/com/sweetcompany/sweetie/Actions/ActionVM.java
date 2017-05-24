package com.sweetcompany.sweetie.Actions;

import android.content.Context;

import com.sweetcompany.sweetie.IPageChanger;
import com.sweetcompany.sweetie.R;

/**
 * Created by ghiro on 08/05/2017.
 */

// TODO abstract class
abstract class ActionVM {

    protected IPageChanger mPageChanger;
    protected Context mContext;

    private String mTitle;
    private String mLastUser;
    private String mDescription;
    private String mDdata; // TODO change format
    private int mType;

    ActionVM() {
    }

    ActionVM(String title, String lastUser, String description, String date, int type) {
        // TODO assertion function true value
        this.mTitle = title;
        this.mLastUser = lastUser;
        this.mDescription = description;
        this.mDdata = date;
        this.mType = type;
    }


    // Setter from Fragment injection
    void setPageChanger(IPageChanger pageChanger) { mPageChanger = pageChanger; }

    void setContext(Context context){
        mContext = context;
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

    public void setType(int type){
        this.mType = type;
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

    public int getType() { return mType; }


    /*** override method ***/

    public abstract void showAction();

    // default implementation; TODO should it be abstract?
    public int getIconId() {
        return R.drawable.action_todo_icon;
    }
}
