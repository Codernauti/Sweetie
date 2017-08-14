package com.sweetcompany.sweetie.todolist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.model.CheckEntryFB;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.util.List;

/**
 * Created by lucas on 04/08/2017.
 */

public class ToDoListFragment extends Fragment implements  ToDoListContract.View, View.OnClickListener,
        ToDoListAdapter.ToDoListAdapterListener {

    private static final String TAG = "ToDoListFragment";

    private ToDoListAdapter toDoListAdapter;
    private Toolbar mToolBar;
    private RecyclerView mToDoListListView;
    private LinearLayoutManager mLinearLayoutManager;
    private CheckBox mCheckBox;
    private EditText mEditText;
    private Button mInputButton;


    private ToDoListContract.Presenter mPresenter;

    public static ToDoListFragment newInstance(Bundle bundle) {
        ToDoListFragment newToDoListFragment = new ToDoListFragment();
        newToDoListFragment.setArguments(bundle);

        return newToDoListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toDoListAdapter = new ToDoListAdapter();
        toDoListAdapter.setListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.todolist_fragment, container, false);

        String titleToDoList = getArguments().getString(ToDoListActivity.TODOLIST_TITLE);
        String toDoListUid = getArguments().getString(ToDoListActivity.TODOLIST_DATABASE_KEY);
        Log.d(TAG, "from Intent TODOLIST_TITLE: " + titleToDoList);
        Log.d(TAG, "from Intent TODOLIST_DATABASE_KEY: " + toDoListUid);

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.todolist_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleToDoList);

        //initialize input widgets

        mCheckBox = (CheckBox) root.findViewById(R.id.todolist_checkBox);
        mEditText = (EditText) root.findViewById(R.id.todolist_text_input);
        mInputButton = (Button) root.findViewById(R.id.todolist_add_button);

        // initialize message's list
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mToDoListListView = (RecyclerView) root.findViewById(R.id.todolist_list);
        mToDoListListView.setLayoutManager(mLinearLayoutManager);
        mToDoListListView.setAdapter(toDoListAdapter);
        mInputButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.todolist_add_button:
                String text = mEditText.getText().toString();
                Log.d(TAG, "text: "+text);
                mEditText.setText("");
                mCheckBox.setChecked(false);
                if (!text.equals("")) {
                    CheckEntryVM checkEntryVM = new CheckEntryVM(CheckEntryVM.THE_MAIN_USER, null, text,
                            DataMaker.get_UTC_DateTime(), mCheckBox.isChecked());
                    mPresenter.addCheckEntry(checkEntryVM);
                }
                break;
        }
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
        mToDoListListView.scrollToPosition(toDoListAdapter.getItemCount() - 1);
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
