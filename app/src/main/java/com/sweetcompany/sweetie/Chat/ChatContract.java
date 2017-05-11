package com.sweetcompany.sweetie.Chat;

/**
 * Created by ghiro on 11/05/2017.
 */

public interface ChatContract {

    interface View {
        void setPresenter(ChatContract.Presenter presenter);
    }

    interface Presenter {
        void start();
        void pause();
        void updateActionsList();
    }
}
