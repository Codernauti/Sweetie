package com.codernauti.sweetie.actionInfo;

import android.net.Uri;


public interface ActionInfoContract {

    interface View {
        void setPresenter(Presenter presenter);
        void updateInfo(ActionInfoVM actionFB);
    }

    interface Presenter {
        void changeTitle(String newTitle);
        void changeImage(Uri imageLocalUri);
        void changePosition(Double latitude, Double longitude, String address);
    }
}
