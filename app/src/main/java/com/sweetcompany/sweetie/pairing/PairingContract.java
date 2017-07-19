package com.sweetcompany.sweetie.pairing;

/**
 * Created by Eduard on 29-Jun-17.
 */

interface PairingContract {

    interface View {
        void setPresenter(PairingContract.Presenter presenter);
        void showMessage(String message);
        void showLoadingProgress();
        void hideLoadingProgress();
        void startDashboardActivity();
    }

    interface Presenter {
        void sendPairingRequest(String partnerPhone);
    }
}
