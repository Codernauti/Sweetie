package com.sweetcompany.sweetie.gallery;

import android.content.Context;

interface GalleryContract {

    interface View {
        void setPresenter(GalleryContract.Presenter presenter);
        void updateGalleryInfo(GalleryVM gallery);

        void addMedia(MediaVM mediaVM);
        void removeMedia(MediaVM mediaVM);
        void changeMedia(MediaVM mediaVM);
        @Deprecated
        void updatePercentUpload(String mediaUid, int perc);
    }
    
    interface Presenter {
        void sendMedia(String localFilePath, String actionUid, Context context);
        void removeMedia(MediaVM media);
    }
}
