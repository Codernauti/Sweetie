package com.sweetcompany.sweetie.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.sweetcompany.sweetie.firebase.FirebaseGalleryController;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.MediaFB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ghiro on 22/07/2017.
 */

class GalleryPresenter implements GalleryContract.Presenter, FirebaseGalleryController.GalleryControllerListener {

    private static final String TAG = "GALLERYPresenter";

    private GalleryContract.View mView;
    private FirebaseGalleryController mController;
    private String mUserMail;   // id of messages of main user

    GalleryPresenter(GalleryContract.View view, FirebaseGalleryController controller, String userMail){
        mView = view;
        mView.setPresenter(this);
        mController = controller;
        mController.addListener(this);

        mUserMail = userMail;
    }

    @Override
    public void sendMedia(MediaVM mediaVM) {
        // TODO: remove down cast -> use Factory method
        PhotoVM photoVM = (PhotoVM) mediaVM;
        MediaFB newMedia = new MediaFB(mUserMail, photoVM.getDescription(), photoVM.getTime(), false, photoVM.getUriLocal(), photoVM.getUriStorage());

        mController.sendMedia(newMedia);
        //mView.updateMedia(mediaVM);
    }

    @Override
    public void onGalleryChanged(GalleryFB gallery) {
        GalleryVM galleryVM = new GalleryVM(gallery.getKey(), gallery.getTitle());
        mView.updateGalleryInfo(galleryVM);
    }

    @Override
    public void onMediaAdded(MediaFB media) {
        MediaVM mediaVM = createPhotoVM(media);
        mView.updateMedia(mediaVM);
    }

    @Override
    public void onMediaRemoved(MediaFB media) {
        MediaVM mediaVM = createPhotoVM(media);
        mView.removeMedia(mediaVM);
    }

    @Override
    public void onMediaChanged(MediaFB media) {
        MediaVM mediaVM = createPhotoVM(media);
        mView.changeMedia(mediaVM);
    }

    /**
     * Convert MediaFB to PhotoVM
     * @param media
     * @return
     */
    private MediaVM createPhotoVM(MediaFB media) {
        // Understand if the message is of Main User
        boolean who = MediaVM.THE_PARTNER;
        if (media.getEmail() != null) {   // TODO remove check in future
            if (media.getEmail().equals(mUserMail)) {
                who = MediaVM.THE_MAIN_USER;
            }
        }
        // Create respective ViewModel
        return new PhotoVM(who, media.getDateTime(), media.getText(), media.getKey(), media.getUriLocal(), media.getUriStorage());
    }
}