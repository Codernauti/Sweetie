package com.sweetcompany.sweetie.Chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sweetcompany.sweetie.Actions.ActionsFragment;
import com.sweetcompany.sweetie.Actions.ActionsPresenter;
import com.sweetcompany.sweetie.R;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatActivity extends AppCompatActivity {

    ChatPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_activity);

        ChatFragment view = new ChatFragment();
        mPresenter = new ChatPresenter(view);
        view.setPresenter(mPresenter);

    }

}
