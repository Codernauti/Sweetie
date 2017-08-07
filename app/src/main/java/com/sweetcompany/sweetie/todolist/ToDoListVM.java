package com.sweetcompany.sweetie.todolist;

/**
 * Created by lucas on 04/08/2017.
 */

public class ToDoListVM {
    private String mKey;
    private String mTitle;

    ToDoListVM(String key, String title) {
        mKey = key;
        mTitle = title;
    }

    String getTitle() {
        return mTitle;
    }
}
