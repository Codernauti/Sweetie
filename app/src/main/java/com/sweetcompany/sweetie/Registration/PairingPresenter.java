package com.sweetcompany.sweetie.Registration;

import android.util.Log;

import com.sweetcompany.sweetie.Firebase.FirebasePairingController;
import com.sweetcompany.sweetie.Firebase.PairingRequestFB;
import com.sweetcompany.sweetie.Firebase.UserFB;

import java.util.List;

/**
 * Created by Eduard on 29-Jun-17.
 */

class PairingPresenter implements PairingContract.Presenter,
        FirebasePairingController.OnFirebasePairingListener {

    private final String mUserPhoneNumber;
    private String mUserUid;
    private PairingContract.View mView;

    private FirebasePairingController mFirebasePairingController;
    private String mParterPhone;

    PairingPresenter(PairingContract.View view, FirebasePairingController controller,
                     String userUid, String userPhoneNumber) {
        mUserUid = userUid;
        mUserPhoneNumber = userPhoneNumber;
        mView = view;
        mView.setPresenter(this);
        mFirebasePairingController = controller;
    }

    @Override
    public void sendPairingRequest(String partnerPhone) {
        if (partnerPhone.equals(mUserPhoneNumber)) {
            mView.showMessage("You can't send a request to yourself");
        }
        else if (!partnerPhone.isEmpty()){
            mView.showMessage("Check if you have pending request...");
            mView.showLoadingProgress();
            mParterPhone = partnerPhone;
            mFirebasePairingController.downloadPairingRequest();
        }
        else {
            mView.showMessage("Please insert the phone number of your partner");
        }
    }

    // Firebase callbacks

    @Override
    public void onDownloadPairingRequestsCompleted(List<PairingRequestFB> userPairingRequests) {
        String partnerUid = getPartnerUidFromPairingRequests(userPairingRequests);
        // CheckUserInput
        if (partnerUid != null) {
            Log.d("PairingPresenter", "The user has already this request from " + mParterPhone);
            mFirebasePairingController.createNewCouple(mUserUid, partnerUid);
        }
        else {
            mFirebasePairingController.searchUserWithPhoneNumber(mParterPhone);
        }

    }

    private String getPartnerUidFromPairingRequests(List<PairingRequestFB> userPairingRequests) {
        for (PairingRequestFB request : userPairingRequests) {
            if (request.getPhoneNumber().equals(mParterPhone)) {
                return request.getKey();
            }
        }
        return null;
    }

    @Override
    public void onSearchUserWithPhoneNumberFinished(UserFB user) {
        mFirebasePairingController.createNewPairingRequest(user, mUserPhoneNumber);
    }

    @Override
    public void onCreateNewCoupleComplete() {
        mView.hideLoadingProgress();
        mView.showMessage("You are now coupled!");
        mView.startDashboardActivity();
    }

    @Override
    public void onCreateNewPairingRequestComplete() {
        mView.hideLoadingProgress();
        mView.showMessage("Request sent successfully");
        mView.startDashboardActivity();
    }

}
