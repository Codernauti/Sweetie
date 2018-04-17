package com.codernauti.sweetie.settings;

import android.net.Uri;

import com.codernauti.sweetie.firebase.FirebaseSettingsController;
import com.codernauti.sweetie.model.UserFB;


public class SettingsPresenter implements SettingsContract.Presenter,
        FirebaseSettingsController.SettingsControllerListener {

    private final SettingsContract.View mView;
    private final FirebaseSettingsController mController;

    public SettingsPresenter(SettingsContract.View view, FirebaseSettingsController controller) {
        mView = view;
        mView.setPresenter(this);

        mController = controller;
        mController.setListener(this);
    }

    @Override
    public void uploadImage(Uri uriImage) {
        mController.changeUserImage(uriImage);
    }

    // controller callback

    @Override
    public void onUserChanged(UserFB user) {

        if (user.isUploadingImg()) {
            mView.setProgressViewsVisible(true);
        } else {
            mView.setProgressViewsVisible(false);
        }

        // TODO: convert UserFB to UserVM
        mView.updateUserInfo(user.getImageUrl(), user.getUsername(), user.getEmail(),
                user.getPhone(), user.getGender());
    }
}
