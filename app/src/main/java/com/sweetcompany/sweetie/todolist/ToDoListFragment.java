package com.sweetcompany.sweetie.todolist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by lucas on 04/08/2017.
 */

public class ToDoListFragment extends Fragment implements  ToDoListContract.View, View.OnClickListener,
        View.OnTouchListener {

    private static final String TAG = "ToDoListFragment";

    public static ToDoListFragment newInstance(Bundle bundle) {
        ToDoListFragment newToDoListFragment = new ToDoListFragment();
        newToDoListFragment.setArguments(bundle);

        return newToDoListFragment;
    }

    @Override
    public void onClick(android.view.View v) {

    }

    @Override
    public boolean onTouch(android.view.View v, MotionEvent event) {
        return false;
    }

    @Override
    public void setPresenter(ToDoListContract.Presenter presenter) {

    }

    @Override
    public void updateCheckEntries(List<CheckEntryVM> checkEntries) {

    }

    @Override
    public void updateChatInfo(ToDoListVM toDoList) {

    }

    @Override
    public void updateCheckEntry(CheckEntryVM checkEntry) {

    }

    @Override
    public void removeCheckEntry(CheckEntryVM checkEntry) {

    }

    @Override
    public void changeCheckEntry(CheckEntryVM checkEntry) {

    }
}
