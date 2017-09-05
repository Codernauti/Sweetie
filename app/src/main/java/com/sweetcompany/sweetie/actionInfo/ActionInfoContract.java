package com.sweetcompany.sweetie.actionInfo;

import android.net.Uri;

/**
 * Created by Eduard on 05-Sep-17.
 */

public interface ActionInfoContract {

    interface View {
        void setPresenter(Presenter presenter);
        void updateInfo(ActionInfoVM actionFB);
        void showImageUploadProgress(int progress);
    }

    interface Presenter {
        void changeTitle(String newTitle);
        void changeImage(Uri imageLocalUri);
        void changePosition(Double latitude, Double longitude);
    }
}
