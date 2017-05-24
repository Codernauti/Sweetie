package com.sweetcompany.sweetie.Chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sweetcompany.sweetie.Actions.ActionsFragment;
import com.sweetcompany.sweetie.Actions.ActionsPresenter;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatActivity extends AppCompatActivity {

    ChatPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        ChatFragment view = (ChatFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.chat_fragment_container);

        if (view == null) {
            view = ChatFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.chat_fragment_container, view);
            transaction.commit();
        }

        String userMail = Utility.getStringPreference(this, Utility.MAIL);
        mPresenter = new ChatPresenter(view, userMail);
    }

}
