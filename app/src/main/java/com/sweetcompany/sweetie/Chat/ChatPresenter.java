package com.sweetcompany.sweetie.Chat;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatPresenter implements ChatContract.Presenter {

    ChatContract.View mView;

    public ChatPresenter(ChatContract.View view){
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void updateActionsList() {

    }
}
