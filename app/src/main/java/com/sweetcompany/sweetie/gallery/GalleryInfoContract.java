package com.sweetcompany.sweetie.gallery;

import android.net.Uri;

import com.sweetcompany.sweetie.model.GalleryFB;

/**
 * Created by Eduard on 04-Sep-17.
 */

interface GalleryInfoContract {

    interface View {
        void setPresenter(Presenter presenter);
        void updateInfo(GalleryVM actionFB);
        void showImageUploadProgress(int progress);
    }

    interface Presenter {
        void changeTitle(String newTitle);
        void changeImage(Uri imageLocalUri);
        void changePosition(Double latitude, Double longitude);
    }
}
