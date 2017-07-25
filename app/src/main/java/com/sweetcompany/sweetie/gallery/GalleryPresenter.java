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
        //

        mUserMail = userMail;
    }

    @Override
    public void sendMedia(MediaVM mediaVM) {
        // TODO: remove down cast -> use Factory method
        PhotoVM photoVM = (PhotoVM) mediaVM;
        String encode;
        encode = encodeBitmap(photoVM.getBitmap());
        MediaFB newMedia = new MediaFB(mUserMail, photoVM.getDescription(), photoVM.getTime(), false, encode);

        mController.sendMedia(newMedia);
    }

    public String encodeBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public Bitmap decodeFirebaseEncode(String encode){
        Bitmap imageBitmap = null;
        try {
            imageBitmap = decodeFromFirebaseBase64(encode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBitmap;
    }

    @Override
    public void bookmarkMedia(MediaVM mediaVM) {
        // TODO: remove down cast -> use Factory method
        /*TextMessageVM msgVM = (TextMessageVM) messageVM;
        MessageFB updateMessage = new MessageFB(mUserMail, msgVM.getText(), msgVM.getTime(), msgVM.isBookmarked());
        updateMessage.setKey(msgVM.getKey());

        mController.updateMessage(updateMessage);*/
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
        Bitmap bitmap = decodeFirebaseEncode(media.getEncode());
        return new PhotoVM(who, media.getDateTime(), media.getText(), media.isBookmarked(), media.getKey(), bitmap);
    }
}