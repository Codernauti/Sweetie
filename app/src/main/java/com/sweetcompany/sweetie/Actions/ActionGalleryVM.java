package com.sweetcompany.sweetie.Actions;

import android.util.Log;

import com.sweetcompany.sweetie.R;

/**
 * Created by Federico Allegro on 24/05/2017.
 */

//TODO complete class
class ActionGalleryVM extends ActionVM {

    ActionGalleryVM(String title, String description, String date) {
        // TODO: complete all fields
        super.setTitle(title);
        super.setTitle(description);
        super.setDataTime(date);
    }

    @Override
    public void showAction() {
        Log.d("ActionGalleryVM", getTitle() + " openAction");

        // For SingleActivity App
        // mPageChanger.changePageTo(0);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_photo_icon;
    }
}
