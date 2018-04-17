package com.codernauti.sweetie.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.codernauti.sweetie.BaseActivity;
import com.codernauti.sweetie.R;
import com.codernauti.sweetie.actionInfo.ActionInfoFragment;
import com.codernauti.sweetie.actionInfo.ActionInfoPresenter;
import com.codernauti.sweetie.firebase.FirebaseActionInfoController;
import com.codernauti.sweetie.model.ToDoListFB;


public class ToDoListInfoActivity extends BaseActivity {

    private static final String TAG = "ChatInfoActivity";

    private static final String TODOLIST_UID_KEY = "toDoListUid";

    private String mToDoListUid;

    private FirebaseActionInfoController<ToDoListFB> mController;
    private ActionInfoPresenter<ToDoListFB> mPresenter;

    public static Intent getStartActivityIntent(Context context, String todoListUid) {
        Intent intent = new Intent(context, ToDoListInfoActivity.class);
        intent.putExtra(TODOLIST_UID_KEY, todoListUid);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_info_activity);


        if (savedInstanceState == null) {   // first opened
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mToDoListUid = savedInstanceState.getString(TODOLIST_UID_KEY);

            Log.d(TAG, "ToDoListUid = " + mToDoListUid);
        }
        else {
            Log.w(TAG, "No savedInstanceState or intentArgs!");
        }


        ActionInfoFragment view = (ActionInfoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.action_info_fragment_container);

        if (view == null) {
            view = ActionInfoFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.action_info_fragment_container, view);
            transaction.commit();
        }

        if (mToDoListUid != null) {
            mController = new FirebaseActionInfoController<>(super.mCoupleUid, mToDoListUid, ToDoListFB.class);
            mPresenter = new ActionInfoPresenter<>(view, mController);
        } else {
            Log.w(TAG, "ToDOList Uid is null, impossible to instantiate Controller and presenter");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mController.attachListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mController.detachListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TODOLIST_UID_KEY, mToDoListUid);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
