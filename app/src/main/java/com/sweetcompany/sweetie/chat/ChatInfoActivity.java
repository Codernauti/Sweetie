package com.sweetcompany.sweetie.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.sweetcompany.sweetie.BaseActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.actionInfo.ActionInfoFragment;
import com.sweetcompany.sweetie.actionInfo.ActionInfoPresenter;
import com.sweetcompany.sweetie.firebase.FirebaseActionInfoController;
import com.sweetcompany.sweetie.model.ChatFB;

/**
 * Created by Eduard on 05-Sep-17.
 */

public class ChatInfoActivity extends BaseActivity {

    private static final String TAG = "ChatInfoActivity";

    private static final String CHAT_UID_KEY = "chatUid";

    private String mChatUid;

    private FirebaseActionInfoController<ChatFB> mController;
    private ActionInfoPresenter<ChatFB> mPresenter;

    public static Intent getStartActivityIntent(Context context, String galleryUid) {
        Intent intent = new Intent(context, ChatInfoActivity.class);
        intent.putExtra(CHAT_UID_KEY, galleryUid);
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
            mChatUid = savedInstanceState.getString(CHAT_UID_KEY);

            Log.d(TAG, "GalleryUid = " + mChatUid);
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

        if (mChatUid != null) {
            mController = new FirebaseActionInfoController<>(super.mCoupleUid, mChatUid, ChatFB.class);
            mPresenter = new ActionInfoPresenter<>(view, mController);
        } else {
            Log.w(TAG, "Chat Uid is null, impossible to instantiate Controller and presenter");
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
        outState.putString(CHAT_UID_KEY, mChatUid);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
