package com.codernauti.sweetie.todolist;

import com.codernauti.sweetie.R;


public class ToDoListButtonVM implements ToDoListItemVM {
    @Override
    public void configViewHolder(ToDoListViewHolder viewHolder){}

    @Override
    public int getIdView() {
        return R.layout.todolist_button_item;
    }
}
