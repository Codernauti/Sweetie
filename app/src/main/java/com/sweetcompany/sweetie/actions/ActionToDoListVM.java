package com.sweetcompany.sweetie.actions;

import android.content.Intent;
import android.util.Log;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.todolist.ToDoListActivity;


class ActionToDoListVM extends ActionVM{
    ActionToDoListVM(String title, String description, String lastUpdateDate, int type, String childKey,
                     String actionKey, int notificationCounter){
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
        return ActionFB.TODOLIST;
    }

    @Override
    public void showAction() {
        Log.d("ActionToDoListVM", getTitle() + " openAction");

        Intent intent = new Intent(mContext, ToDoListActivity.class);
        intent.putExtra(ToDoListActivity.TODOLIST_TITLE, super.getTitle());
        intent.putExtra(ToDoListActivity.TODOLIST_DATABASE_KEY, super.getChildUid());
        intent.putExtra(ToDoListActivity.ACTION_DATABASE_KEY, super.getKey());

        mContext.startActivity(intent);
    }

    @Override
    public int getIconId() {
        return R.drawable.action_todolist_icon;
    }

    @Override
    public int getAvatarTextIdColor() {
        return R.color.todolist_main_color;
    }
}
