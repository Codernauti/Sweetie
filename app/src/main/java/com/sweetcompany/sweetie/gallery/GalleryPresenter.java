package com.sweetcompany.sweetie.gallery;

import com.sweetcompany.sweetie.firebase.FirebaseGalleryController;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.MediaFB;


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

        String newMediaUID = mController.sendMedia(newMedia);
        newMedia.setKey(newMediaUID);
        photoVM.setKey(newMediaUID);
        mView.updateMedia(photoVM);
    }

    @Override
    public void removeMedia(MediaVM media) {
        mController.removeMedia(media.getKey(), media.getUriStorage());
    }

    // controller callbacks

    @Override
    public void onGalleryChanged(GalleryFB gallery) {
        GalleryVM galleryVM = new GalleryVM(gallery.getKey(), gallery.getTitle(), gallery.getDate(), gallery.getUriCover(), gallery.getLatitude(), gallery.getLongitude());
        mView.updateGalleryInfo(galleryVM);
    }

    @Override
    public void onMediaAdded(MediaFB media) {
        //MediaVM mediaVM = MediaConverter.createMediaVM(media, mUserMail);
        MediaVM mediaVM = createPhotoVM(media);
        mView.updateMedia(mediaVM);
    }

    @Override
    public void onMediaRemoved(MediaFB media) {
        //MediaVM mediaVM = MediaConverter.createMediaVM(media, mUserMail);
        MediaVM mediaVM = createPhotoVM(media);
        mView.removeMedia(mediaVM);
    }

    @Override
    public void onMediaChanged(MediaFB media) {
        //MediaVM mediaVM = MediaConverter.createMediaVM(media, mUserMail);
        MediaVM mediaVM = createPhotoVM(media);
        mView.changeMedia(mediaVM);
    }

    @Override
    public void onUploadPercent(MediaFB media, int perc){
        mView.updatePercentUpload(media.getKey(), perc);
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
        return new PhotoVM(who, media.getDateTime(), media.getText(), media.getUriLocal(), media.getUriStorage(), 0, media.getKey());
    }

}