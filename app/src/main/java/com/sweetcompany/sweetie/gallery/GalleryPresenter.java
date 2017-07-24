package com.sweetcompany.sweetie.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.sweetcompany.sweetie.firebase.FirebaseGalleryController;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.PhotoFB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static android.R.attr.bitmap;

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
    public void sendPhoto(PhotoVM photo) {
        // TODO: remove down cast -> use Factory method
        /*TextMessageVM messageVM = (TextMessageVM)message;
        PhotoFB newMessage = new PhotoFB(mUserMail, messageVM.getText(), messageVM.getTime(), messageVM.isBookmarked());
        */
        String encode;
        encode = encodeBitmap(photo.getBitmap());
        PhotoFB newPhoto = new PhotoFB(mUserMail, "", "datetime ***", false, encode);

        mController.uploadPhoto(newPhoto);
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
    public void bookmarkPhoto(PhotoVM messageVM) {
        // TODO: remove down cast -> use Factory method
        /*TextMessageVM msgVM = (TextMessageVM) messageVM;
        MessageFB updateMessage = new MessageFB(mUserMail, msgVM.getText(), msgVM.getTime(), msgVM.isBookmarked());
        updateMessage.setKey(msgVM.getKey());

        mController.updateMessage(updateMessage);*/
    }

    @Override
    public void onGalleryChanged(PhotoFB gallery) {

    }

    @Override
    public void onPhotoAdded(PhotoFB photo) {
        PhotoVM photoVM = createPhotoVM(photo);
        mView.updatePhoto(photoVM);
    }

    @Override
    public void onPhotoRemoved(PhotoFB message) {

    }

    @Override
    public void onPhotoChanged(PhotoFB message) {

    }


    /**
     * Convert PhotoFB to PhotoVM
     * @param photo
     * @return
     */
    private PhotoVM createPhotoVM(PhotoFB photo) {
        // Understand if the photo is of Main User
        //boolean who = MessageVM.THE_PARTNER;
        /*if (photo.getEmail() != null) {   // TODO remove check in future
            if (photo.getEmail().equals(mUserMail)) {
                who = MessageVM.THE_MAIN_USER;
            }
        }*/
        // Create respective ViewModel

        Bitmap bitmap = decodeFirebaseEncode(photo.getEncode());

        return new PhotoVM(photo.getText(), photo.getDateTime(), photo.isBookmarked(), bitmap);
    }
}