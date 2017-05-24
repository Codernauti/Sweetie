package com.sweetcompany.sweetie.Registration;

import android.content.Context;



import com.sweetcompany.sweetie.Firebase.FirebaseRegisterController;
import com.sweetcompany.sweetie.Firebase.PairingRequest;
import com.sweetcompany.sweetie.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 22/05/2017.
 */

public class RegisterPresenter implements RegisterContract.Presenter, FirebaseRegisterController.OnFirebaseRegisterDataChange{
    public static final String TAG = "Registration.presenter";
    FirebaseRegisterController mFirebaseController;
    RegisterContract.View mView;

    RegisterPresenter(RegisterContract.View view){
        mView = view;
        mView.setPresenter(this);
        mFirebaseController = FirebaseRegisterController.getInstance();
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
                Utility.getStringPreference(mContext,Utility.PHONENUMBER),
                Utility.getStringPreference(mContext,Utility.MAIL),
                Boolean.valueOf(Utility.getStringPreference(mContext,Utility.GENDER)));
        mFirebaseController.saveUserData(token,user);
    }

    @Override
    public void start() {
        mFirebaseController.attachNetworkDatabase();
        mFirebaseController.addListener(this);
    }

    @Override
    public void pause() {
        mFirebaseController.detachNetworkDatabase();
        mFirebaseController.removeListener(this);
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


}
