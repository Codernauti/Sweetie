package com.sweetcompany.sweetie.actions;

import android.content.Intent;
import android.util.Log;

import com.sweetcompany.sweetie.gallery.GalleryActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.model.ActionFB;

/**
 * Created by Federico Allegro on 24/05/2017.
 */

//TODO complete class
class ActionGalleryVM extends ActionVM {

    ActionGalleryVM(String title, String description, String date, int type, String childKey, String actionKey) {
        // TODO: complete all fields
        super.setTitle(title);
        super.setDescription(description);
        super.setDataTime(date);
        super.setType(type);
        super.setChildUid(childKey);
        super.setKey(actionKey);
    }

    @Override
    public int getChildType() {
        return ActionFB.GALLERY;
    }

    @Override
    public void showAction() {
        Log.d("ActionGalleryVM", getTitle() + " openAction");

        Intent intent = new Intent(mContext, GalleryActivity.class);
        intent.putExtra(GalleryActivity.GALLERY_TITLE, super.getTitle());
        intent.putExtra(GalleryActivity.GALLERY_DATABASE_KEY, super.getChildUid());
        intent.putExtra(GalleryActivity.ACTION_DATABASE_KEY, super.getKey());

        mContext.startActivity(intent);

        // For SingleActivity App
        // mPageChanger.changePageTo(0);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_photo_icon;
    }
}
