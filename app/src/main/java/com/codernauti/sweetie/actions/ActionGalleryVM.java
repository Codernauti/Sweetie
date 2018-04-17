package com.codernauti.sweetie.actions;

import android.content.Intent;
import android.util.Log;

import com.codernauti.sweetie.gallery.GalleryActivity;
import com.codernauti.sweetie.R;
import com.codernauti.sweetie.model.ActionFB;


//TODO complete class
class ActionGalleryVM extends ActionVM {

    ActionGalleryVM(String title, String description, String lastUpdateDate, int type, String childKey,
                    String actionKey, int notificationCounter) {
        // TODO: complete all fields
        super.setTitle(title);
        super.setDescription(description);
        super.setLastUpdateDate(lastUpdateDate);
        super.setType(type);
        super.setChildUid(childKey);
        super.setKey(actionKey);
        super.setNotificationCounter(notificationCounter);
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
        intent.putExtra(GalleryActivity.ACTION_DATABASE_KEY, super.getKey());

        mContext.startActivity(intent);

        // For SingleActivity App
        // mPageChanger.changePageTo(0);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_photo_icon;
    }

    @Override
    public int getAvatarTextIdColor() {
        return R.color.gallery_main_color;
    }
}
