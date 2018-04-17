package com.codernauti.sweetie.chatDiary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.codernauti.sweetie.BaseActivity;
import com.codernauti.sweetie.R;
import com.codernauti.sweetie.firebase.FirebaseChatDiaryController;


public class ChatDiaryActivity extends BaseActivity {

    private static final String TAG = "ChatDiaryActivity";

    public static final String ACTION_DIARY_DATE = "ActionDiaryDate";
    public static final String ACTION_DIARY_UID = "ActionDiaryUid";

    private ChatDiaryFragment mView;
    private ChatDiaryPresenter mPresenter;
    private FirebaseChatDiaryController mController;

    private String mActionDiaryDate;
    private String mActionDiaryUid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        if (savedInstanceState == null) {   // first opened
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mActionDiaryDate = savedInstanceState.getString(ACTION_DIARY_DATE);
            mActionDiaryUid = savedInstanceState.getString(ACTION_DIARY_UID);
        } else {
            Log.w(TAG, "No savedInstanceState or intentArgs!");
        }

        mView = (ChatDiaryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.chat_fragment_container);

        if (mView == null) {
            mView = ChatDiaryFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.chat_fragment_container, mView);
            transaction.commit();
        }

        if (mActionDiaryDate != null && mActionDiaryUid != null) {
            mController = new FirebaseChatDiaryController(super.mCoupleUid, mActionDiaryDate,
                    mActionDiaryUid);
            mPresenter = new ChatDiaryPresenter(mView, mController, super.mUserUid);
        }
        else {
            Log.w(TAG, "Impossible to create ChatDiaryController and ChatDiaryPresenter because actionDiaryUid OR actionDiaryDate are NULL");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mController.attachListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mController.detachListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ACTION_DIARY_DATE, mActionDiaryDate);
        outState.putString(ACTION_DIARY_UID, mActionDiaryUid);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

}
