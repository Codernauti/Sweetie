package com.sweetcompany.sweetie.Registration;

import android.content.Context;


import com.sweetcompany.sweetie.Firebase.FirebaseLoginController;
import com.sweetcompany.sweetie.Firebase.FirebasePairingController;
import com.sweetcompany.sweetie.Firebase.FirebaseRegisterController;
import com.sweetcompany.sweetie.Firebase.UserFB;
import com.sweetcompany.sweetie.Utils.Utility;

/**
 * Created by lucas on 22/05/2017.
 */

public class RegisterPresenter implements RegisterContract.Presenter,
                                        FirebaseLoginController.OnFirebaseLoginChecked,
                                        FirebaseRegisterController.OnFirebaseUserDataFound {

    public static final String TAG = "RegisterPresenter";

    private FirebaseRegisterController mFirebaseRegisterController;
    private FirebaseLoginController mFirebaseLoginController;
    private RegisterContract.View mView;

    RegisterPresenter(RegisterContract.View view){
        mView = view;
        mView.setPresenter(this);
        mFirebaseRegisterController = FirebaseRegisterController.getInstance();
        mFirebaseLoginController = FirebaseLoginController.getInstance();
    }

    public RegisterPresenter setView(RegisterContract.View view){
        mView = null;
        mView = view;
        mView.setPresenter(this);
        return this;
    }

    @Override
    public void saveUserData(Context mContext){
        UserVM user= new UserVM(Utility.getStringPreference(mContext,Utility.USER_UID),
                Utility.getStringPreference(mContext,Utility.USERNAME),
                Utility.getStringPreference(mContext,Utility.PHONE_NUMBER),
                Utility.getStringPreference(mContext,Utility.MAIL),
                Boolean.valueOf(Utility.getStringPreference(mContext,Utility.GENDER)));
        mFirebaseRegisterController.saveUserData(user);
    }

    @Override
    public void attachUserCheckListener(String key) {
        mFirebaseLoginController.retrieveUserDataFromQuery(key);
        mFirebaseLoginController.addUserCheckListener(this);
    }

    // Firebase callback

    @Override
    public void notifyUserFound(UserFB userFB) {
        UserVM userVM = new UserVM(userFB.getKey(), userFB.getUsername(), userFB.getPhone(),
                userFB.getEmail(), userFB.isGender());
        mView.notifyUser(userVM);
    }

    @Override
    public void notifyUserChecked(UserFB userFB) {
        if(userFB != null){
        mView.notifyUserCheck(new UserVM(userFB.getKey(), userFB.getUsername(), userFB.getPhone(),
                userFB.getEmail(), userFB.isGender()));
        }
        else
            mView.notifyUserCheck(null);
    }


}
