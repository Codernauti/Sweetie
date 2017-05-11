package com.sweetcompany.sweetie.Actions;

import android.util.Log;

/**
 * Created by Eduard on 10/05/2017.
 */

// TODO complete class
public class ActionChatVM extends ActionVM {

    public ActionChatVM(String title) {
        // TODO: complete all fields
        super.setTitle(title);
    }

    @Override
    public void showAction() {
        Log.d("ActionChatVM", getTitle() + " override work!!!");

        //mView.showCalendarFragment();
        mPageChanger.changePageTo(2);
    }
}
