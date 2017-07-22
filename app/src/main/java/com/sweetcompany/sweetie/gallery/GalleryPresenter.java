package com.sweetcompany.sweetie.gallery;

import com.sweetcompany.sweetie.firebase.FirebaseGalleryController;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.PhotoFB;

/**
 * Created by ghiro on 22/07/2017.
 */

class GalleryPresenter implements GalleryContract.Presenter, FirebaseGalleryController.GalleryControllerListener {

    private static final String TAG = "ChatPresenter";

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

        mController.sendMessage(newMessage);*/
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
    public void onGalleryChanged(PhotoFB chat) {

    }

    @Override
    public void onPhotoAdded(PhotoFB message) {

    }

    @Override
    public void onPhotoRemoved(PhotoFB message) {

    }

    @Override
    public void onPhotoChanged(PhotoFB message) {

    }


    /**
     * Convert MessageFB to TextMessageVM
     * @param message
     * @return
     */
    /*private MessageVM createMessageVM(MessageFB message) {
        // Understand if the message is of Main User
        boolean who = MessageVM.THE_PARTNER;
        if (message.getEmail() != null) {   // TODO remove check in future
            if (message.getEmail().equals(mUserMail)) {
                who = MessageVM.THE_MAIN_USER;
            }
        }
        // Create respective ViewModel
        return new TextMessageVM(message.getText(), who, message.getDateTime(),
                message.isBookmarked(), message.getKey());
    }*/
}