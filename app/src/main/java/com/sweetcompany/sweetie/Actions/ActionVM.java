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

    ActionVM() {
    }

    ActionVM(String title, String lastUser, String description, String date) {
        // TODO assertion function true value
        this.mTitle = title;
        this.mLastUser = lastUser;
        this.mDescription = description;
        this.mDdata = date;
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


    /*** override method ***/

    public abstract void showAction();

    // default implementation; TODO should it be abstract?
    public int getIconId() {
        return R.drawable.action_todo_icon;
    }
}
