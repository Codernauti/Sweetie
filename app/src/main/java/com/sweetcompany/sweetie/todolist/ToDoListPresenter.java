package com.sweetcompany.sweetie.todolist;

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

    }

    @Override
    public void checkedCheckEntry(CheckEntryVM checkEntry) {

    }

    @Override
    public void onToDoListChanged(ToDoListFB todolist) {

    }

    @Override
    public void onCheckEntryAdded(CheckEntryFB checkEntry) {

    }

    @Override
    public void onCheckEntryRemoved(CheckEntryFB checkEntry) {

    }

    @Override
    public void onCheckEntryChanged(CheckEntryFB checkEntry) {

    }
}
