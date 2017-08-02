package com.sweetcompany.sweetie.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebaseChatController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    // key for Intent extras
    public static final String CHAT_DATABASE_KEY = "ChatDatabaseKey";
    public static final String CHAT_TITLE = "ChatTitle";    // For offline user
    public static final String ACTION_DATABASE_KEY = "ActionDatabaseKey";

    private String mChatKey;
    private String mActionKey;

    private ChatContract.Presenter mPresenter;
    private FirebaseChatController mController;
    private ChatContract.View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        if (savedInstanceState == null) {   // first opened
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mChatKey = savedInstanceState.getString(CHAT_DATABASE_KEY);
            mActionKey = savedInstanceState.getString(ACTION_DATABASE_KEY);

            Log.d(TAG, "from Intent CHAT_TITLE: " +
                    savedInstanceState.getString(CHAT_TITLE));
            Log.d(TAG, "from Intent CHAT_DATABASE_KEY: " +
                    savedInstanceState.getString(CHAT_DATABASE_KEY));
            Log.d(TAG, "from Intent CHAT_ACTION_KEY: " +
                    savedInstanceState.getString(ACTION_DATABASE_KEY));
        }
        else {
            Log.w(TAG, "No savedInstanceState or intentArgs!");
        }

        mView = (ChatFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.chat_fragment_container);

        if (mView == null) {
            mView = ChatFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.chat_fragment_container, (ChatFragment)mView);
            transaction.commit();
        }

        String userMail = Utility.getStringPreference(this, Utility.MAIL);
        String coupleUid = Utility.getStringPreference(this, Utility.COUPLE_UID);

        if (mChatKey != null) {
            mController = new FirebaseChatController(coupleUid, mChatKey, mActionKey);
            mPresenter = new ChatPresenter(mView, mController, userMail);
        }
        else {
            Log.w(TAG, "Impossible to create ChatController and ChatPresenter because chatKey is NULL");
        }

        mController.attachListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mController.attachListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mController.detachListeners();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CHAT_DATABASE_KEY, mChatKey);
        outState.putString(ACTION_DATABASE_KEY, mActionKey);
    }

    @Override
    public void onBackPressed() {
        boolean isEmojiKeyboardOpen = mView.hideKeyboardPlaceholder();
        if (!isEmojiKeyboardOpen) {
            // go back
            super.onBackPressed();
        }
    }
}
