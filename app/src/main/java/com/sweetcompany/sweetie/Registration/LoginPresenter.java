package com.sweetcompany.sweetie.Registration;

import com.sweetcompany.sweetie.Firebase.FirebaseLoginController;
import com.sweetcompany.sweetie.Firebase.UserFB;
import com.sweetcompany.sweetie.Utils.Utility;

/**
 * Created by Eduard on 13-Jul-17.
 */

public class LoginPresenter implements RegisterContract.LoginPresenter,
        FirebaseLoginController.FbLoginControllerListener{

    public static final String TAG = "RegisterPresenter";

    private FirebaseLoginController mFirebaseLoginController;
    private RegisterContract.LoginView mView;

    LoginPresenter(RegisterContract.LoginView view, FirebaseLoginController loginController){
        mView = view;
        mView.setPresenter(this);
        mFirebaseLoginController = loginController;
    }

    @Override
    public void attachUserCheckListener(String key) {
        mFirebaseLoginController.retrieveUserDataFromQuery(key);
    }

    // Firebase callback

    @Override
    public void onUserDownloadFinished(UserFB userFB) {
        mView.setProgressBarVisible(false);
    }


}
