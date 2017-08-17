package com.sweetcompany.sweetie.actions;

import android.content.Intent;
import android.util.Log;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.geogift.GeogiftMakerActivity;

/**
 * Created by ghiro on 07/08/2017.
 */

public class ActionGeogiftVM extends ActionVM {

    boolean isVisited;

    ActionGeogiftVM(String title, String description, String date, int type, String childKey, String actionKey, boolean visited) {
        // TODO: complete all fields
        super.setTitle(title);
        super.setDescription(description);
        super.setDataTime(date);
        super.setType(type);
        super.setChildKey(childKey);
        super.setActionKey(actionKey);
        isVisited = visited;
    }

    public boolean getVisited(){
        return isVisited;
    }

    @Override
    public void showAction() {
        Log.d("ActionGeogiftVM", getTitle() + " openAction");

        Intent intent = new Intent(mContext, GeogiftMakerActivity.class);
        intent.putExtra(GeogiftMakerActivity.GEOGIFT_TITLE, super.getTitle());
        //intent.putExtra(GeogiftMakerActivity.GEOGIFT_DATABASE_KEY, super.getChildKey());
        //intent.putExtra(GeogiftMakerActivity.ACTION_DATABASE_KEY, super.getActionKey());

        mContext.startActivity(intent);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_gift_icon;
    }
}
