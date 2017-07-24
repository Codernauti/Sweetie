package com.sweetcompany.sweetie.gallery;

import android.graphics.Bitmap;
import java.util.List;

/**
 * Created by ghiro on 22/07/2017.
 */

interface GalleryContract {

    interface View {
        void setPresenter(GalleryContract.Presenter presenter);
        void updatePhotos(List<PhotoVM> photos);
        void updateGalleryInfo(GalleryVM gallery);

        void updatePhoto(PhotoVM photoVM);
        void removePhoto(PhotoVM photoVM);
        void changePhoto(PhotoVM photoVM);

    }
    
    interface Presenter {
        void sendPhoto(PhotoVM photo);
        void bookmarkPhoto(PhotoVM photo);
    }
}
