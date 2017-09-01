package com.sweetcompany.sweetie.chat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.sweetcompany.sweetie.BaseActivity;
import com.sweetcompany.sweetie.firebase.FirebaseChatController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatActivity extends BaseActivity {

    private static final String TAG = "ChatActivity";

    // key for Intent extras
    public static final String CHAT_DATABASE_KEY = "ChatDatabaseKey";
    public static final String CHAT_TITLE = "ChatTitle";    // For offline user
    public static final String ACTION_DATABASE_KEY = "ActionDatabaseKey";

    private String mChatKey;
    private String mChatTitle;
    private String mActionKey;

    private boolean mBound;

    private ChatContract.Presenter mPresenter;
    private FirebaseChatController mController;
    private ChatContract.View mView;
    private MessagesMonitorService mService;

    /// Defines callbacks for service binding, passed to bindService()
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MessagesMonitorService.LocalBinder binder = (MessagesMonitorService.LocalBinder) service;
            mService = binder.getServiceInstance();

            mService.cancelNotification();
            mService.resetCacheFor(mChatKey);

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        if (savedInstanceState == null) {   // first opened
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mChatTitle = savedInstanceState.getString(CHAT_TITLE);
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

        if (mChatKey != null) {
            mController = new FirebaseChatController(super.mCoupleUid, mChatKey, mChatTitle,
                    mActionKey, super.mPartnerUid);
            mPresenter = new ChatPresenter(mView, mController, super.mUserEmail);
        } else {
            Log.w(TAG, "Impossible to create ChatController and ChatPresenter because chatKey is NULL");
        }

        mController.attachListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utility.saveStringPreference(this, SharedPrefKeys.CHAT_FOREGROUND_UID, mChatKey);
        bindService(new Intent(this, MessagesMonitorService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Utility.removePreference(this, SharedPrefKeys.CHAT_FOREGROUND_UID);

        if (mBound) {
            unbindService(mConnection);
            mBound = false;
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
