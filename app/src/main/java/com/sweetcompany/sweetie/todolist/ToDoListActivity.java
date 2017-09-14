package com.sweetcompany.sweetie.todolist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.sweetcompany.sweetie.BaseActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseToDoListController;
import com.sweetcompany.sweetie.utils.Utility;



public class ToDoListActivity extends BaseActivity {
    private static final String TAG = "ToDoListActivity";

    // key for Intent extras
    public static final String TODOLIST_DATABASE_KEY = "ToDoListDatabaseKey";
    public static final String TODOLIST_TITLE = "ToDoListTitle";    // For offline user
    public static final String ACTION_DATABASE_KEY = "ActionDatabaseKey";

    private String mToDoListKey;

    private ToDoListContract.Presenter mPresenter;
    private FirebaseToDoListController mController;
    private ToDoListContract.View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_activity);

        if (savedInstanceState == null) {   // first opened
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mToDoListKey = savedInstanceState.getString(TODOLIST_DATABASE_KEY);;

            Log.d(TAG, "from Intent TODOLIST_TITLE: " +
                    savedInstanceState.getString(TODOLIST_TITLE));
            Log.d(TAG, "from Intent ACTION_DATABASE_KEY: " +
                    savedInstanceState.getString(ACTION_DATABASE_KEY));
        } else {
            Log.w(TAG, "No savedInstanceState or intentArgs!");
        }

        mView = (ToDoListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.todolist_fragment_container);

        if (mView == null) {
            mView = ToDoListFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.todolist_fragment_container, (ToDoListFragment)mView);
            transaction.commit();
        }

        if (mToDoListKey != null) {
            mController = new FirebaseToDoListController(super.mCoupleUid, mToDoListKey,
                                                            super.mUserUid, super.mPartnerUid);
            mPresenter = new ToDoListPresenter(mView, mController, super.mUserUid);

            mController.attachListeners();
        } else {
            Log.w(TAG, "Impossible to create ToDoListController and ToDoListPresenter because ToDoListKey is NULL");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.detachListeners();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TODOLIST_DATABASE_KEY, mToDoListKey);
    }
}
