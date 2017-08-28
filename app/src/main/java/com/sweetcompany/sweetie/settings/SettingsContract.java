package com.sweetcompany.sweetie.settings;

/**
 * Created by Eduard on 28-Aug-17.
 */

interface SettingsContract {

    interface View {
        void setPresenter(Presenter presenter);
        void updateUserInfo(String userImageUri, String username, String email, String telephone, boolean gender);
    }

    interface Presenter {

    }
}
