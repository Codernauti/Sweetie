package com.sweetcompany.sweetie.settings;

import com.sweetcompany.sweetie.firebase.FirebaseSettingsController;
import com.sweetcompany.sweetie.model.UserFB;

/**
 * Created by Eduard on 28-Aug-17.
 */

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
    public void onUserChanged(UserFB user) {
        // TODO: convert UserFB to UserVM
        mView.updateUserInfo(user.getImageUrl(), user.getUsername(), user.getEmail(),
                user.getPhone(), user.getGender());
    }

    @Override
    public void onImageUploadProgress(int progress) {

    }
}
