package com.codernauti.sweetie.pairing;


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
