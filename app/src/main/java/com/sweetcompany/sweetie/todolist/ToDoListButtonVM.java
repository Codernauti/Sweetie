package com.sweetcompany.sweetie.todolist;

import com.sweetcompany.sweetie.R;

/**
 * Created by lucas on 06/09/2017.
 */

public class ToDoListButtonVM implements ToDoListItemVM {
    @Override
    public void configViewHolder(ToDoListViewHolder viewHolder){}

    @Override
    public int getIdView() {
        return R.layout.todolist_button_item;
    }
}
