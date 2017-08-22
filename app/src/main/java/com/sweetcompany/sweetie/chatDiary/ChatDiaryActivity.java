package com.sweetcompany.sweetie.chatDiary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseChatDiaryController;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by Eduard on 22-Aug-17.
 */

public class ChatDiaryActivity extends AppCompatActivity {

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

        String userMail = Utility.getStringPreference(this, Utility.MAIL);
        String coupleUid = Utility.getStringPreference(this, Utility.COUPLE_UID);

        if (mActionDiaryDate != null && mActionDiaryUid != null) {
            mController = new FirebaseChatDiaryController(coupleUid, mActionDiaryDate, mActionDiaryUid);
            mPresenter = new ChatDiaryPresenter(mView, mController, userMail);
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
