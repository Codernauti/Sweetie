package com.sweetcompany.sweetie.Chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sweetcompany.sweetie.Actions.ActionNewChatFragment;
import com.sweetcompany.sweetie.Actions.ActionsFragment;
import com.sweetcompany.sweetie.Actions.ActionsPresenter;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    // key for Intent extras
    public static final String CHAT_DATABASE_KEY = "ChatDatabaseKey";
    public static final String CHAT_TITLE = "ChatTitle";    // For offline user

    ChatPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        String chatKey = null;
        if (savedInstanceState == null) { // first Activity open
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                chatKey = bundle.getString(CHAT_DATABASE_KEY);

                Log.d(TAG, "from Intent CHAT_TITLE: " +
                        bundle.getString(CHAT_TITLE));
                Log.d(TAG, "from Intent CHAT_DATABASE_KEY: " +
                        bundle.getString(CHAT_DATABASE_KEY));
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
        mPresenter = new ChatPresenter(view, userMail, chatKey);
    }

}
