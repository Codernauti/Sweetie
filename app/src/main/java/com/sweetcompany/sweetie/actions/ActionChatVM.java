package com.sweetcompany.sweetie.actions;

import android.content.Intent;
import android.util.Log;

import com.sweetcompany.sweetie.Chat.ChatActivity;
import com.sweetcompany.sweetie.R;

/**
 * Created by Eduard on 10/05/2017.
 */

// TODO complete class
class ActionChatVM extends ActionVM {

    ActionChatVM(String title, String description, String date, int type, String childKey, String actionKey) {
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
        Log.d("ActionChatVM", getTitle() + " openAction");

        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra(ChatActivity.CHAT_TITLE, super.getTitle());
        intent.putExtra(ChatActivity.CHAT_DATABASE_KEY, super.getChildKey());
        intent.putExtra(ChatActivity.ACTION_DATABASE_KEY, super.getActionKey());

        mContext.startActivity(intent);

        // Or in a SingleActivity App
        //mPageChanger.changePageTo(2);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_chat_icon;
    }
}
