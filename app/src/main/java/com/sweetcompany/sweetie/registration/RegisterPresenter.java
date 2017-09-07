package com.sweetcompany.sweetie.registration;


import com.sweetcompany.sweetie.firebase.FirebaseRegisterController;
import com.sweetcompany.sweetie.model.UserFB;

/**
 * Created by lucas on 22/05/2017.
 */

public class RegisterPresenter implements RegisterContract.RegisterPresenter,
        FirebaseRegisterController.Listener {

    public static final String TAG = "RegisterPresenter";

    private FirebaseRegisterController mController;
    private RegisterContract.RegisterView mView;

    RegisterPresenter(RegisterContract.RegisterView view, FirebaseRegisterController controller){
        mView = view;
        mView.setPresenter(this);
        mController = controller;
    }

    @Override
    public void saveUserData(String uid, String email, String username, String phoneNumber, boolean gender) {
        UserFB user= new UserFB(username, email, phoneNumber, gender);
        user.setKey(uid);
        mController.saveUserData(user);
    }

    // Firebase callback

    // TODO: move this callback to activity
    @Override
    public void onUserPushed() {
        mView.showNextScreen();
        // TODO: give other feedback to the user
    }
}
