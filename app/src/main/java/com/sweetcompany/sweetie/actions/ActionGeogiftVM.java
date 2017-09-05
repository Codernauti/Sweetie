package com.sweetcompany.sweetie.actions;

import android.content.Intent;
import android.util.Log;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.geogift.GeogiftDoneActivity;
import com.sweetcompany.sweetie.model.ActionFB;

/**
 * Created by ghiro on 07/08/2017.
 */

public class ActionGeogiftVM extends ActionVM {

    boolean isVisited;

    ActionGeogiftVM(String title, String description, String lastUpdateDate, int type, String childKey,
                    String actionKey, boolean visited, int notificationCounter) {
        // TODO: complete all fields
        super.setTitle(title);
        super.setDescription(description);
        super.setLastUpdateDate(lastUpdateDate);
        super.setType(type);
        super.setChildUid(childKey);
        super.setKey(actionKey);
        isVisited = visited;
        super.setNotificationCounter(notificationCounter);
    }

    public boolean getVisited(){
        return isVisited;
    }

    @Override
    public int getChildType() {
        return ActionFB.GEOGIFT;
    }

    @Override
    public void showAction() {
        Log.d("ActionGeogiftVM", getTitle() + " openAction");

        Intent intent = new Intent(mContext, GeogiftDoneActivity.class);
        intent.putExtra(GeogiftDoneActivity.GEOGIFT_TITLE, super.getTitle());
        intent.putExtra(GeogiftDoneActivity.GEOGIFT_DATABASE_KEY, super.getChildUid());

        mContext.startActivity(intent);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_gift_icon;
    }

    @Override
    public int getAvatarTextIdColor() {
        return R.color.background_uploading_geogift;
    }
}
