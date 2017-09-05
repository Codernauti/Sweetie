package com.sweetcompany.sweetie.actions;

import android.content.Intent;
import android.util.Log;

import com.sweetcompany.sweetie.chat.ChatActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.model.ActionFB;

/**
 * Created by Eduard on 10/05/2017.
 */

// TODO complete class
class ActionChatVM extends ActionVM {

    ActionChatVM(String title, String description, String lastUpdateDate, int type, String childKey,
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
        return ActionFB.CHAT;
    }

    @Override
    public void showAction() {
        Log.d("ActionChatVM", getTitle() + " openAction");

        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra(ChatActivity.CHAT_TITLE, super.getTitle());
        intent.putExtra(ChatActivity.CHAT_DATABASE_KEY, super.getChildUid());
        intent.putExtra(ChatActivity.ACTION_DATABASE_KEY, super.getKey());

        mContext.startActivity(intent);

        // Or in a SingleActivity App
        //mPageChanger.changePageTo(2);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_chat_icon;
    }

    @Override
    public int getAvatarTextIdColor() {
        return R.color.chat_main_color;
    }
}
