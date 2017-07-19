package com.sweetcompany.sweetie.registration;

/**
 * Created by lucas on 16/05/2017.
 */

interface RegisterContract {

    // StepOne
    interface LoginView {
        void setPresenter(LoginPresenter presenter);
        void setProgressBarVisible(boolean b);
    }

    interface LoginPresenter {
        void attachUserCheckListener(String key);
    }

    // StepTwo
    interface RegisterView {
        void setPresenter(RegisterPresenter presenter);
        void startPairingActivity();
    }

    interface RegisterPresenter {
        void saveUserData(String uid, String email, String username, String phonNumber, boolean gender);
    }

}

