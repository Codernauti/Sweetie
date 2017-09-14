package com.sweetcompany.sweetie.login;


interface LoginContract {

    interface LoginView {
        void setPresenter(LoginPresenter presenter);
        void setProgressBarVisible(boolean b);
    }

    interface LoginPresenter {
        void attachUserCheckListener(String key);
    }

}
