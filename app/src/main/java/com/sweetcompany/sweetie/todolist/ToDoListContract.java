package com.sweetcompany.sweetie.todolist;

import com.sweetcompany.sweetie.model.CheckEntryFB;

import java.util.List;

/**
 * Created by lucas on 04/08/2017.
 */

public interface ToDoListContract {
    interface View {
        void setPresenter(ToDoListContract.Presenter presenter);
        void updateToDoListInfo(ToDoListVM toDoList);
        void addCheckEntry(CheckEntryVM checkEntry);
        void removeCheckEntry(CheckEntryVM checkEntry);
        void changeCheckEntry(CheckEntryVM checkEntry);

    }
    interface Presenter {
        void addCheckEntry(CheckEntryVM checkEntry);
        void removeCheckEntry(String key);
        void changeCheckEntry(CheckEntryVM checkEntry);
        void checkedCheckEntry(CheckEntryVM checkEntry);
    }
}
