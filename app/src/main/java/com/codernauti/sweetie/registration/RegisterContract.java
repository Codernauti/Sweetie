package com.codernauti.sweetie.registration;

import android.net.Uri;


interface RegisterContract {

    // SetInfoFragment
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

