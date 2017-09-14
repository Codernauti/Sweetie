package com.sweetcompany.sweetie.todolist;

import com.sweetcompany.sweetie.R;


public class ToDoListButtonVM implements ToDoListItemVM {
    @Override
    public void configViewHolder(ToDoListViewHolder viewHolder){}

    @Override
    public int getIdView() {
        return R.layout.todolist_button_item;
    }
}
