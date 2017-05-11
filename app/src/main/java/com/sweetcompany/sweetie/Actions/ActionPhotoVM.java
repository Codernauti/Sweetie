package com.sweetcompany.sweetie.Actions;

import android.util.Log;

import com.sweetcompany.sweetie.R;

/**
 * Created by Eduard on 11/05/2017.
 */

//TODO complete class
public class ActionPhotoVM extends ActionVM {

    public ActionPhotoVM(String title) {
        // TODO: complete all fields
        super.setTitle(title);
    }

    @Override
    public void showAction() {
        Log.d("ActionPhotoVM", getTitle() + " override work!!!");

        //mView.showCalendarFragment();
        mPageChanger.changePageTo(0);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_photo_icon;
    }
}
