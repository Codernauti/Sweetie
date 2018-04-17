package com.codernauti.sweetie.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;

import com.codernauti.sweetie.R;
import com.codernauti.sweetie.utils.DataMaker;



public class ToDoListFragment extends Fragment implements  ToDoListContract.View,
        ToDoListAdapter.ToDoListAdapterListener {

    private static final String TAG = "ToDoListFragment";

    private ToDoListAdapter toDoListAdapter;
    private Toolbar mToolBar;
    private RecyclerView mToDoListListView;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean entryAddedFromButton = false;
    private String mToDoListUid;

    private ToDoListContract.Presenter mPresenter;

    public static ToDoListFragment newInstance(Bundle bundle) {
        ToDoListFragment newToDoListFragment = new ToDoListFragment();
        newToDoListFragment.setArguments(bundle);
        return newToDoListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        toDoListAdapter = new ToDoListAdapter();
        toDoListAdapter.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.todolist_fragment, container, false);

        String titleToDoList = getArguments().getString(ToDoListActivity.TODOLIST_TITLE);
        mToDoListUid = getArguments().getString(ToDoListActivity.TODOLIST_DATABASE_KEY);
        Log.d(TAG, "from Intent TODOLIST_TITLE: " + titleToDoList);

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.todolist_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        ActionBar actionBar = parentActivity.getSupportActionBar();
        if (actionBar != null) {
            parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            parentActivity.getSupportActionBar().setTitle(titleToDoList);
        }

        // initialize message's list
        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mToDoListListView = (RecyclerView) root.findViewById(R.id.todolist_list);
        mToDoListListView.setItemAnimator(null);
        mToDoListListView.setLayoutManager(mLinearLayoutManager);
        mToDoListListView.setAdapter(toDoListAdapter);

        return root;
    }


    // Menu management

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_info_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_info: {
                Intent intent = ToDoListInfoActivity.getStartActivityIntent(
                        getContext(), mToDoListUid);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
        return false;
    }



    @Override
    public void setPresenter(ToDoListContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void updateToDoListInfo(ToDoListVM toDoList) {
        mToolBar.setTitle(toDoList.getTitle());
    }

    @Override
    public void addCheckEntry(CheckEntryVM checkEntry) {
        if(entryAddedFromButton) {
            checkEntry.setFocus(true);
            entryAddedFromButton = false;
        }
        toDoListAdapter.addCheckEntry(checkEntry);
    }

    @Override
    public void removeCheckEntry(CheckEntryVM checkEntry) {
        toDoListAdapter.removeCheckEntry(checkEntry);
    }

    @Override
    public void changeCheckEntry(CheckEntryVM checkEntry) {
        toDoListAdapter.changeCheckEntry(checkEntry);
    }


    // adapter callbacks

    @Override
    public void onCheckEntryClicked(CheckEntryVM checkEntry) {
        mPresenter.checkedCheckEntry(checkEntry);
    }


    @Override
    public void onCheckEntryUnfocused(CheckEntryVM checkEntry) {
        mPresenter.changeCheckEntry(checkEntry);
    }

    @Override
    public void onCheckEntryRemove(String key, int vhPositionToFocus) {
        if(vhPositionToFocus != -1) {
            ((CheckEntryViewHolder) mToDoListListView.findViewHolderForAdapterPosition(vhPositionToFocus)).setFocus();
        }
        mPresenter.removeCheckEntry(key);
    }

    @Override
    public void onAddButtonClicked() {
        entryAddedFromButton = true;
        CheckEntryVM checkEntryVM = new CheckEntryVM(CheckEntryVM.THE_MAIN_USER, null, "",
                DataMaker.get_UTC_DateTime(),false);
        mPresenter.addCheckEntry(checkEntryVM);
    }
}
