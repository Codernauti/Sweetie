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
    private String mUserUid;

    GalleryPresenter(GalleryContract.View view, FirebaseGalleryController controller, String userUid){
        mUserUid = userUid;

        mController = controller;
        mController.addListener(this);
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void sendMedia(MediaVM mediaVM) {
        // TODO: remove down cast -> use Factory method
        PhotoVM photoVM = (PhotoVM) mediaVM;
        MediaFB newMedia = new MediaFB(mUserUid, photoVM.getDescription(), photoVM.getTime(), false,
                photoVM.getUriStorage(), photoVM.isUploading());

        mController.uploadMedia(newMedia);

        /*String newMediaUID = mController.sendMedia(newMedia);
        photoVM.setKey(newMediaUID);
        photoVM.setPercent(0);
        mView.addMedia(photoVM);*/
    }

    @Override
    public void removeMedia(MediaVM media) {
        mController.removeMedia(media.getKey(), media.getUriStorage());
    }

    // controller callbacks

    @Override
    public void onGalleryChanged(GalleryFB gallery) {
        GalleryVM galleryVM = new GalleryVM(gallery.getKey(), gallery.getTitle(),
                gallery.getCreationDate(), gallery.getUriCover(), gallery.getLatitude(), gallery.getLongitude());
        mView.updateGalleryInfo(galleryVM);
    }

    @Override
    public void onMediaAdded(MediaFB media) {
        //MediaVM mediaVM = MediaConverter.createMediaVM(media, mUserMail);
        MediaVM mediaVM = createPhotoVM(media);
        mView.addMedia(mediaVM);
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
    @Deprecated
    public void onUploadPercent(MediaFB media, int perc){
        mView.updatePercentUpload(media.getKey(), perc);
    }

    /**
     * Convert MediaFB to PhotoVM
     * @param media
     * @return
     */

    private MediaVM createPhotoVM(MediaFB media) {
        // Understand if the media is of Main User
        boolean who = MediaVM.THE_PARTNER;
        if (media.getUserUid().equals(mUserUid)) {
            who = MediaVM.THE_MAIN_USER;
        }
        
        return new PhotoVM(who, media.getDateTime(), media.getText(), media.getUriStorage(),
                media.getKey(), media.isUploading());
    }

}