package com.codernauti.sweetie.todolist;


public class ToDoListVM {
    private String mKey;
    private String mTitle;
    private String mCreationDate;

    ToDoListVM(String key, String title) {
        mKey = key;
        mTitle = title;
    }

    String getTitle() {
        return mTitle;
    }

    public String getCreationDate() {
        return mCreationDate;
    }
    public void setCreationDate(String mCreationDate) {
        this.mCreationDate = mCreationDate;
    }
}
