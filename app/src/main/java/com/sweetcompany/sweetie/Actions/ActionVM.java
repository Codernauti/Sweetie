package com.sweetcompany.sweetie.Actions;

import android.util.Log;

import com.sweetcompany.sweetie.IPageChanger;

/**
 * Created by ghiro on 08/05/2017.
 */

// TODO abstract class
public class ActionVM {

    protected ActionsContract.View mView;
    protected IPageChanger mPageChanger;

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
    public void showAction() {
        Log.d("ActionVM", "ahowAction doesn't override");
    }
}
