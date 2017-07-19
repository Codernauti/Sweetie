package com.sweetcompany.sweetie.actions;

import android.content.Intent;
import android.util.Log;

import com.sweetcompany.sweetie.gallery.GalleryActivity;
import com.sweetcompany.sweetie.R;

/**
 * Created by Federico Allegro on 24/05/2017.
 */

//TODO complete class
class ActionGalleryVM extends ActionVM {

    ActionGalleryVM(String title, String description, String date, int type) {
        // TODO: complete all fields
        super.setTitle(title);
        super.setDescription(description);
        super.setDataTime(date);
        super.setType(type);
    }

    @Override
    public void showAction() {
        Log.d("ActionGalleryVM", getTitle() + " openAction");

        Intent intent = new Intent(mContext, GalleryActivity.class);
        mContext.startActivity(intent);

        // For SingleActivity App
        // mPageChanger.changePageTo(0);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_photo_icon;
    }
}
