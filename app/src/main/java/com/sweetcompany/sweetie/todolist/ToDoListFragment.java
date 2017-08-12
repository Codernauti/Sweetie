package com.sweetcompany.sweetie.todolist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 04/08/2017.
 */

public class ToDoListFragment extends Fragment implements  ToDoListContract.View, View.OnClickListener,
        View.OnTouchListener, ToDoListAdapter.ToDoListAdapterListener {

    private static final String TAG = "ToDoListFragment";

    private ToDoListAdapter toDoListAdapter;
    private Toolbar mToolBar;
    private ListView mToDoListListView;

    private ToDoListContract.Presenter mPresenter;

    public static ToDoListFragment newInstance(Bundle bundle) {
        ToDoListFragment newToDoListFragment = new ToDoListFragment();
        newToDoListFragment.setArguments(bundle);

        return newToDoListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //toDoListAdapter = new ToDoListAdapter(getContext(),new ArrayList<CheckEntryVM>());
        //toDoListAdapter.setToDoListAdapterListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.todolist_fragment, container, false);

       /* String titleToDoList = getArguments().getString(ToDoListActivity.TODOLIST_TITLE);
        String toDoListUid = getArguments().getString(ToDoListActivity.TODOLIST_DATABASE_KEY);
        Log.d(TAG, "from Intent TODOLIST_TITLE: " + titleToDoList);
        Log.d(TAG, "from Intent TODOLIST_DATABASE_KEY: " + toDoListUid);*/

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.todolist_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //parentActivity.getSupportActionBar().setTitle(titleToDoList);


        //mToDoListListView = (ListView) root.findViewById(R.id.todolist_listview);
        //mToDoListListView.setAdapter(toDoListAdapter);

        return root;
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
        mPresenter = presenter;
    }

    @Override
    public void updateCheckEntries(List<CheckEntryVM> checkEntries) {
        toDoListAdapter.updateCheckEntriesList(checkEntries);
    }

    @Override
    public void updateToDoListInfo(ToDoListVM toDoList) {
        mToolBar.setTitle(toDoList.getTitle());
    }

    @Override
    public void updateCheckEntry(CheckEntryVM checkEntry) {
        toDoListAdapter.addCheckEntry(checkEntry);
        mToDoListListView.smoothScrollToPosition(toDoListAdapter.getCount() - 1);
    }

    @Override
    public void removeCheckEntry(CheckEntryVM checkEntry) {
        toDoListAdapter.removeCheckEntry(checkEntry);
    }

    @Override
    public void changeCheckEntry(CheckEntryVM checkEntry) {
        toDoListAdapter.changeCheckEntry(checkEntry);
    }

    @Override
    public void onCheckEntryClicked(CheckEntryVM checkEntry) {
        mPresenter.checkedCheckEntry(checkEntry);
    }

    @Override
    public void onCheckEntryLongClicked(int position, List<CheckEntryVM> checkEntriesVM) {

    }
}
