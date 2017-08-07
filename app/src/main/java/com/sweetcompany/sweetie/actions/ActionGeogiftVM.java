package com.sweetcompany.sweetie.actions;

import android.content.Intent;
import android.util.Log;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.geogift.GeogiftActivity;

/**
 * Created by ghiro on 07/08/2017.
 */

public class ActionGeogiftVM extends ActionVM {

    ActionGeogiftVM(String title, String description, String date, int type, String childKey, String actionKey) {
        // TODO: complete all fields
        super.setTitle(title);
        super.setDescription(description);
        super.setDataTime(date);
        super.setType(type);
        super.setChildKey(childKey);
        super.setActionKey(actionKey);
    }

    @Override
    public void showAction() {
        Log.d("ActionGeogiftVM", getTitle() + " openAction");

        Intent intent = new Intent(mContext, GeogiftActivity.class);
        intent.putExtra(GeogiftActivity.GEOGIFT_TITLE, super.getTitle());
        intent.putExtra(GeogiftActivity.GEOGIFT_DATABASE_KEY, super.getChildKey());
        intent.putExtra(GeogiftActivity.ACTION_DATABASE_KEY, super.getActionKey());

        mContext.startActivity(intent);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_gift_icon;
    }
}
