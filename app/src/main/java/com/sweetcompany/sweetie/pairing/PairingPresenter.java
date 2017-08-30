package com.sweetcompany.sweetie.pairing;

import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebasePairingController;
import com.sweetcompany.sweetie.model.PairingRequestFB;
import com.sweetcompany.sweetie.model.UserFB;

import java.util.List;

/**
 * Created by Eduard on 29-Jun-17.
 */

class PairingPresenter implements PairingContract.Presenter,
        FirebasePairingController.PairingControllerListener {

    private static final String TAG = "PairingPresenter";

    private final String mUserUsername;
    private final String mUserPhoneNumber;
    private final String mUserPairingRequestSent;   // It keep trace of previous pairing request
    private final PairingContract.View mView;

    private final FirebasePairingController mController;

    private String mParterPhone;

    PairingPresenter(PairingContract.View view, FirebasePairingController controller,
                     String userUsername, String userPhoneNumber, String userPairingRequestSent) {
        mUserUsername = userUsername;
        mUserPhoneNumber = userPhoneNumber;
        mUserPairingRequestSent = userPairingRequestSent;

        mView = view;
        mView.setPresenter(this);

        if (!mUserPairingRequestSent.equals("error")) {
            // TODO
            // mView.showMessagePairingRequestAlreadySent();
        }

        mController = controller;
    }

    @Override
    public void sendPairingRequest(String partnerPhone) {
        // TODO: check user input

        if (mUserPairingRequestSent.equals("error")) {
            // TODO: go ahead
            Log.d(TAG, "mUserPairingRequestSent == error");
        }
        else {
            // TODO: warning the user
            Log.d(TAG, "show advise");
        }

        if (partnerPhone.equals(mUserPhoneNumber)) {
            mView.showMessage("You can't send a request to yourself");
        }
        else if (!partnerPhone.isEmpty()){
            mView.showMessage("Check if you have pending request...");
            mView.showLoadingProgress();
            mParterPhone = partnerPhone;
            mController.downloadPairingRequest();
        }
        else {
            mView.showMessage("Please insert the phone number of your partner");
        }
    }

    // Firebase callbacks

    @Override
    public void onDownloadPairingRequestsCompleted(List<PairingRequestFB> userPairingRequests) {
        PairingRequestFB partnerPairingRequest = getPartnerPairingRequest(userPairingRequests);
        if (partnerPairingRequest != null) {
            Log.d(TAG, "Implicitly couple because the user has already this request from " + mParterPhone);
            mController.createNewCouple(partnerPairingRequest);
        }
        else {
            mController.searchUserWithPhoneNumber(mParterPhone);
        }

    }

    private PairingRequestFB getPartnerPairingRequest(List<PairingRequestFB> userPairingRequests) {
        for (PairingRequestFB request : userPairingRequests) {
            if (request.getPhoneNumber().equals(mParterPhone)) {
                return request;
            }
        }
        return null;
    }

    @Override
    public void onSearchUserWithPhoneNumberFinished(UserFB user) {
        if (user != null) {
            mController.createNewPairingRequest(user, mUserUsername, mUserPhoneNumber, mUserPairingRequestSent);
        } else {
            mView.hideLoadingProgress();
            // TODO: tell to the user no phone number found, send an invite to Sweetie
            mView.showMessage("The user you are looking for is not already register to Sweetie");
            // TODO: show an alert where the user can try another number or go to Dashboard
            mView.startDashboardActivity();
        }
    }

    @Override
    public void onCreateNewCoupleComplete() {
        mView.hideLoadingProgress();
    }

    @Override
    public void onCreateNewPairingRequestComplete() {
        mView.hideLoadingProgress();
        mView.showMessage("Request sent successfully");
        mView.startDashboardActivity();
    }

}
