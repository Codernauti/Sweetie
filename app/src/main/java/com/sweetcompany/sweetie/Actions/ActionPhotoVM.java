package com.sweetcompany.sweetie.Actions;

import android.util.Log;

import com.sweetcompany.sweetie.R;

/**
 * Created by Eduard on 11/05/2017.
 */

//TODO complete class
class ActionPhotoVM extends ActionVM {

    ActionPhotoVM(String title, String description) {
        // TODO: complete all fields
        super.setTitle(title);
        super.setTitle(description);
    }

    @Override
    public void showAction() {
        Log.d("ActionPhotoVM", getTitle() + " openAction");

        // For SingleActivity App
        // mPageChanger.changePageTo(0);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_photo_icon;
    }
}
