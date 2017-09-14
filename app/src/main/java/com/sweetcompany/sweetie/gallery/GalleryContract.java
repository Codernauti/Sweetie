package com.sweetcompany.sweetie.gallery;

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
        void sendMedia(MediaVM media);
        void removeMedia(MediaVM media);
    }
}
