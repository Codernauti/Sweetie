package com.sweetcompany.sweetie.actions;

import android.content.Intent;
import android.util.Log;

import com.sweetcompany.sweetie.todolist.ToDoListActivity;

/**
 * Created by lucas on 04/08/2017.
 */

public class ActionToDoListVM extends ActionVM{
    ActionToDoListVM(String title, String description, String date, int type, String childKey, String actionKey){
        super.setTitle(title);
        super.setDescription(description);
        super.setDataTime(date);
        super.setType(type);
        super.setChildKey(childKey);
        super.setActionKey(actionKey);
    }

    @Override
    public void showAction() {
        Log.d("ActionToDoListVM", getTitle() + " openAction");

        Intent intent = new Intent(mContext, ToDoListActivity.class);
        intent.putExtra(ToDoListActivity.TODOLIST_TITLE, super.getTitle());
        intent.putExtra(ToDoListActivity.TODOLIST_DATABASE_KEY, super.getChildKey());
        intent.putExtra(ToDoListActivity.ACTION_DATABASE_KEY, super.getActionKey());

        mContext.startActivity(intent);
    }
}
