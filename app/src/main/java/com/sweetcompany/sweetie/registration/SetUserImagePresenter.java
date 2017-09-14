package com.sweetcompany.sweetie.registration;


import android.net.Uri;

import com.sweetcompany.sweetie.firebase.FirebaseSettingsController;
import com.sweetcompany.sweetie.model.UserFB;


class SetUserImagePresenter implements RegisterContract.SetUserImagePresenter,
        FirebaseSettingsController.SettingsControllerListener {

    FirebaseSettingsController mController;
    RegisterContract.SetUserImageView mView;

    SetUserImagePresenter(RegisterContract.SetUserImageView view,
                          FirebaseSettingsController controller) {
        mController = controller;
        mController.setListener(this);

        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void uploadImage(Uri imgLocalUri) {
        mController.changeUserImage(imgLocalUri);
    }


    // Controller callbacks

    @Override
    public void onUserChanged(UserFB user) {
        if (user.isUploadingImg()) {
            mView.setProgressViewsVisible(true);
        } else {
            mView.setProgressViewsVisible(false);
            mView.showImage(user.getImageUrl());
        }
    }

}
