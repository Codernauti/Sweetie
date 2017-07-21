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

    private final String mUserPhoneNumber;
    private final String mUserPairingRequestSent;
    private final PairingContract.View mView;

    private final FirebasePairingController mController;

    private String mParterPhone;

    PairingPresenter(PairingContract.View view, FirebasePairingController controller,
                     String userPhoneNumber, String userPairingRequestSent) {
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
        String partnerUid = getPartnerUidFromPairingRequests(userPairingRequests);
        if (partnerUid != null) {
            Log.d(TAG, "Implicitly couple because the user has already this request from " + mParterPhone);
            mController.createNewCouple(partnerUid);
        }
        else {
            mController.searchUserWithPhoneNumber(mParterPhone);
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
        mController.createNewPairingRequest(user, mUserPhoneNumber, mUserPairingRequestSent);
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
