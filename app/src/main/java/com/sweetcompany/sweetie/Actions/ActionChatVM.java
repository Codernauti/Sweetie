package com.sweetcompany.sweetie.Actions;

import android.content.Intent;
import android.util.Log;

import com.sweetcompany.sweetie.Chat.ChatActivity;
import com.sweetcompany.sweetie.R;

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

        Intent intent = new Intent(mContext, ChatActivity.class);
        mContext.startActivity(intent);

        //mView.showCalendarFragment();
        //mPageChanger.changePageTo(2);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_chat_icon;
    }
}
