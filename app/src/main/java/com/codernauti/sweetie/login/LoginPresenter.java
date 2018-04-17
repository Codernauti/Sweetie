package com.codernauti.sweetie.login;

import com.codernauti.sweetie.firebase.FirebaseLoginController;
import com.codernauti.sweetie.model.UserFB;


class LoginPresenter implements LoginContract.LoginPresenter,
        FirebaseLoginController.FbLoginControllerListener {

    public static final String TAG = "RegisterPresenter";

    private FirebaseLoginController mFirebaseLoginController;
    private LoginContract.LoginView mView;

    LoginPresenter(LoginContract.LoginView view, FirebaseLoginController loginController){
        mView = view;
        mView.setPresenter(this);
        mFirebaseLoginController = loginController;
    }

    @Override
    public void attachUserCheckListener(String key) {
        mFirebaseLoginController.retrieveUserDataFromQuery(key);
    }

    // Controller callback

    @Override
    public void onUserDownloadFinished(UserFB userFB) {
        mView.setProgressBarVisible(false);
    }


}
