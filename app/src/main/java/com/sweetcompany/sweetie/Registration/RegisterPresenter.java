package com.sweetcompany.sweetie.Registration;

import android.app.Activity;
import android.content.Context;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.sweetcompany.sweetie.Firebase.FirebaseLoginController;
import com.sweetcompany.sweetie.Firebase.FirebasePairingController;
import com.sweetcompany.sweetie.Firebase.FirebaseRegisterController;
import com.sweetcompany.sweetie.Firebase.PairingRequest;
import com.sweetcompany.sweetie.Firebase.SweetUser;
import com.sweetcompany.sweetie.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 22/05/2017.
 */

public class RegisterPresenter implements RegisterContract.Presenter,FirebaseLoginController.OnFirebaseLoginChecked, FirebasePairingController.OnFirebasePairingDataChange, FirebaseRegisterController.OnFirebaseUserDataFound{
    public static final String TAG = "Registration.presenter";
    FirebaseRegisterController mFirebaseRegisterController;
    FirebaseLoginController mFirebaseLoginController;
    FirebasePairingController mFirebasePairingController;
    RegisterContract.View mView;

    RegisterPresenter(RegisterContract.View view){
        mView = view;
        mView.setPresenter(this);
        mFirebaseRegisterController = FirebaseRegisterController.getInstance();
        mFirebaseLoginController = FirebaseLoginController.getInstance();
        mFirebasePairingController = FirebasePairingController.getInstance();
    }


    public RegisterPresenter setView(RegisterContract.View view){
        mView = null;
        mView = view;
        mView.setPresenter(this);
        return this;
    }

    @Override
    public void saveUserData(Context mContext){
        String token = Utility.getStringPreference(mContext,Utility.TOKEN);
        UserVM user= new UserVM(Utility.getStringPreference(mContext,Utility.USERNAME),
                Utility.getStringPreference(mContext,Utility.PHONE_NUMBER),
                Utility.getStringPreference(mContext,Utility.MAIL),
                Boolean.valueOf(Utility.getStringPreference(mContext,Utility.GENDER)));
        mFirebaseRegisterController.saveUserData(token,user);
    }

    @Override
    public void savePairingRequest(PairingRequestVM pairingRequest) {
        mFirebasePairingController.saveRequest(pairingRequest);
    }

    @Override
    public void attachUserDataListener(String orderByType,String equalsToData) {
        mFirebaseRegisterController.retrieveUserDataFromQuery(orderByType,equalsToData);
        mFirebaseRegisterController.addUserDataListener(this);
    }

    @Override
    public void attachUserCheckListener(String key) {
        mFirebaseLoginController.retrieveUserDataFromQuery(key);
        mFirebaseLoginController.addUserCheckListener(this);
    }

    @Override
    public void deletePairingRequest(String keyPairingRequest) {
        mFirebasePairingController.deletePairingRequest(keyPairingRequest);
    }

    @Override
    public void start() {
        mFirebasePairingController.attachNetworkDatabase();
        mFirebasePairingController.addListener(this);
    }

    @Override
    public void pause() {
        mFirebasePairingController.detachNetworkDatabase();
        mFirebasePairingController.removeListener(this);
    }

    @Override
    public void notifyNewRequests(List<PairingRequest> pairingRequests) {
        List<PairingRequestVM> requestsVM = new ArrayList<>();
        for (PairingRequest rqst : pairingRequests) {
            PairingRequestVM rqstVM = new PairingRequestVM(rqst.getSenderNumber(),rqst.getReceiverNumber());
            rqstVM.setKey(rqst.getKey());
            requestsVM.add(rqstVM);
        }
        mView.updateRequest(requestsVM);
    }

    @Override
    public void notifyUserFound(SweetUser sweetUser) {
        mView.notifyUser(new UserVM(sweetUser));
    }


    @Override
    public void notifyUserChecked(SweetUser user) {
        if(user != null){
        mView.notifyUserCheck(new UserVM(user));
        }
        else
            mView.notifyUserCheck(null);
    }
}
