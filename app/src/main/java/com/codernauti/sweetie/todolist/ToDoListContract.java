package com.codernauti.sweetie.todolist;


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
