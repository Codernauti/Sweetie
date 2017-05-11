package com.sweetcompany.sweetie.Actions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.sweetcompany.sweetie.IPageChanger;
import com.sweetcompany.sweetie.R;

/**
 * Created by ghiro on 08/05/2017.
 */

// TODO abstract class
public abstract class ActionVM {

    protected ActionsContract.View mView;
    protected IPageChanger mPageChanger;
    protected Context mContext;

    private String mTitle;
    private String mDescription;
    private String mDdata; // TODO change format
    private String mType; // TODO string || int ?

    public ActionVM() {
    }

    public ActionVM(String title, String description, String date, String type) {
        // TODO assertion function true value
        this.mTitle = title;
        this.mDescription = description;
        this.mDdata = date;
        this.mType = type;
    }

    /*** SETTER ***/

    public void setView(ActionsContract.View view) {
        mView = view;
    }

    public void setPageChanger(IPageChanger pageChanger) { mPageChanger = pageChanger; }

    public void setContext(Context context){
        mContext = context;
    }

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


    /* TEST */
    public abstract void showAction();


    public int getIconId() {
        return R.drawable.action_todo_icon;
    }
}
