package com.sweetcompany.sweetie.settings;

import android.net.Uri;


interface SettingsContract {

    interface View {
        void setPresenter(Presenter presenter);
        void updateUserInfo(String userImageUri, String username, String email, String telephone, boolean gender);
        void setProgressViewsVisible(boolean show);
    }

    interface Presenter {
        void uploadImage(Uri uriImage);
    }
}
