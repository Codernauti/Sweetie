package com.sweetcompany.sweetie.chat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebaseChatController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.gallery.GalleryAdapter;
import com.sweetcompany.sweetie.gallery.PhotoVM;
import com.sweetcompany.sweetie.gallery.VolleyController;
import com.sweetcompany.sweetie.utils.Utility;

import java.util.ArrayList;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    // key for Intent extras
    public static final String CHAT_DATABASE_KEY = "ChatDatabaseKey";
    public static final String CHAT_TITLE = "ChatTitle";    // For offline user
    public static final String ACTION_DATABASE_KEY = "ActionDatabaseKey";

    private ChatPresenter mPresenter;
    private FirebaseChatController mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        String chatKey = null;
        String actionKey = null;
        if (savedInstanceState == null) { // first Activity open
            Bundle chatBundle = getIntent().getExtras();
            if (chatBundle != null) {
                chatKey = chatBundle.getString(CHAT_DATABASE_KEY);
                actionKey = chatBundle.getString(ACTION_DATABASE_KEY);

                Log.d(TAG, "from Intent CHAT_TITLE: " +
                        chatBundle.getString(CHAT_TITLE));
                Log.d(TAG, "from Intent CHAT_DATABASE_KEY: " +
                        chatBundle.getString(CHAT_DATABASE_KEY));
                Log.d(TAG, "from Intent CHAT_ACTION_KEY: " +
                        chatBundle.getString(ACTION_DATABASE_KEY));
            }
            else {
                Log.w(TAG, "getIntent FAILED!");
            }
        } else {
            // TODO: restore data from savedInstanceState
        }

        ChatFragment view = (ChatFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.chat_fragment_container);

        if (view == null) {
            view = ChatFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.chat_fragment_container, view);
            transaction.commit();
        }

        String userMail = Utility.getStringPreference(this, Utility.MAIL);
        String coupleUid = Utility.getStringPreference(this, Utility.COUPLE_UID);

        if (chatKey != null) {
            mController = new FirebaseChatController(coupleUid, chatKey, actionKey);
            mPresenter = new ChatPresenter(view, mController, userMail);
        }
        else {
            Log.w(TAG, "Impossible to create ChatController and ChatPresenter because chatKey is NULL");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mController.attachListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mController.detachListeners();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
