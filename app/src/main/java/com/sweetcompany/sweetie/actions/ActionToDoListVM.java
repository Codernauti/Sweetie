package com.sweetcompany.sweetie.actions;

import android.content.Intent;
import android.util.Log;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.todolist.ToDoListActivity;

/**
 * Created by lucas on 04/08/2017.
 */

class ActionToDoListVM extends ActionVM{
    ActionToDoListVM(String title, String description, String date, int type, String childKey, String actionKey){
        super.setTitle(title);
        super.setDescription(description);
        super.setDataTime(date);
        super.setType(type);
        super.setChildUid(childKey);
        super.setKey(actionKey);
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
}
