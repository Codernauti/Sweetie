package com.sweetcompany.sweetie.registration;

import android.net.Uri;


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
        void showNextScreen();
    }

    interface RegisterPresenter {
        void saveUserData(String uid, String email, String username, String phonNumber, boolean gender);
    }

    // StepThree
    interface SetUserImageView {
        void setPresenter(SetUserImagePresenter presenter);
        void setProgressViewsVisible(boolean visible);
        void showNextScreen();
        void showImage(String imageUrl);
    }

    interface SetUserImagePresenter {
        void uploadImage(Uri imgLocalUri);
    }

}

