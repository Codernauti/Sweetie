package com.sweetcompany.sweetie.todolist;

import android.app.AlertDialog;

import com.sweetcompany.sweetie.firebase.FirebaseToDoListController;
import com.sweetcompany.sweetie.model.CheckEntryFB;
import com.sweetcompany.sweetie.model.ToDoListFB;

/**
 * Created by lucas on 04/08/2017.
 */

public class ToDoListPresenter implements ToDoListContract.Presenter, FirebaseToDoListController.ToDoListControllerListener {


    private static final String TAG = "ChatPresenter";

    private ToDoListContract.View mView;
    private FirebaseToDoListController mController;
    private String mUserMail;   // id of checkEntries of main user

    ToDoListPresenter(ToDoListContract.View view, FirebaseToDoListController controller, String userMail){
        mView = view;
        mView.setPresenter(this);
        mController = controller;
        mController.addListener(this);

        mUserMail = userMail;
    }

    @Override
    public void addCheckEntry(CheckEntryVM checkEntry) {
        CheckEntryFB newCheckEntry = new CheckEntryFB(mUserMail, checkEntry.getText(),
                checkEntry.isChecked(),checkEntry.getTime());

        mController.addCheckEntry(newCheckEntry);
    }

    @Override
    public void removeCheckEntry(CheckEntryVM checkEntry) {
        CheckEntryFB removeCheckEntry = new CheckEntryFB(mUserMail, checkEntry.getText(),
                checkEntry.isChecked(),checkEntry.getTime());
        removeCheckEntry.setKey(checkEntry.getKey());
        mController.removeCheckEntry(removeCheckEntry);
    }

    @Override
    public void changeCheckEntry(CheckEntryVM checkEntry) {
        CheckEntryFB updateCheckEntry = new CheckEntryFB(mUserMail, checkEntry.getText(),
                checkEntry.isChecked(),checkEntry.getTime());
        updateCheckEntry.setKey(checkEntry.getKey());

        mController.updateCheckEntry(updateCheckEntry);
    }

    @Override
    public void editCheckEntry(CheckEntryVM checkEntry) {
        mView.editCheckEntry(checkEntry);
    }

    @Override
    public void checkedCheckEntry(CheckEntryVM checkEntry) {
        CheckEntryFB updateCheckEntry = new CheckEntryFB(mUserMail, checkEntry.getText(),
                checkEntry.isChecked(),checkEntry.getTime());
        updateCheckEntry.setKey(checkEntry.getKey());

        mController.updateCheckEntry(updateCheckEntry);
    }


    @Override
    public void onToDoListChanged(ToDoListFB todolist) {
        ToDoListVM toDoListVM = new ToDoListVM(todolist.getKey(), todolist.getTitle());
        mView.updateToDoListInfo(toDoListVM);
    }

    @Override
    public void onCheckEntryAdded(CheckEntryFB checkEntry) {
        boolean who = CheckEntryVM.THE_PARTNER;
        if (checkEntry.getEmail().equals(mUserMail)) {
            who = CheckEntryVM.THE_MAIN_USER;
        }
        CheckEntryVM checkEntryVM = new CheckEntryVM(who,checkEntry.getKey(),checkEntry.getText(),
                checkEntry.getDateTime(),checkEntry.isChecked());

        mView.updateCheckEntry(checkEntryVM);
    }

    @Override
    public void onCheckEntryRemoved(CheckEntryFB checkEntry) {
        boolean who = CheckEntryVM.THE_PARTNER;
        if (checkEntry.getEmail().equals(mUserMail)) {
            who = CheckEntryVM.THE_MAIN_USER;
        }
        CheckEntryVM checkEntryVM = new CheckEntryVM(who,checkEntry.getKey(),checkEntry.getText(),
                checkEntry.getDateTime(),checkEntry.isChecked());
        mView.removeCheckEntry(checkEntryVM);
    }

    @Override
    public void onCheckEntryChanged(CheckEntryFB checkEntry) {
        boolean who = CheckEntryVM.THE_PARTNER;
        if (checkEntry.getEmail().equals(mUserMail)) {
            who = CheckEntryVM.THE_MAIN_USER;
        }
        CheckEntryVM checkEntryVM = new CheckEntryVM(who,checkEntry.getKey(),checkEntry.getText(),
                checkEntry.getDateTime(),checkEntry.isChecked());
        mView.changeCheckEntry(checkEntryVM);
    }
}
