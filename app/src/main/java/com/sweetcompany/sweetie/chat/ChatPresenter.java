package com.sweetcompany.sweetie.chat;

import com.sweetcompany.sweetie.model.ChatFB;
import com.sweetcompany.sweetie.firebase.FirebaseChatController;
import com.sweetcompany.sweetie.model.MessageFB;

/**
 * Created by ghiro on 11/05/2017.
 */

class ChatPresenter implements ChatContract.Presenter, FirebaseChatController.ChatControllerListener {

    private static final String TAG = "ChatPresenter";

    private ChatContract.View mView;
    private FirebaseChatController mController;
    private String mUserMail;   // id of messages of main user

    ChatPresenter(ChatContract.View view, FirebaseChatController controller, String userMail){
        mView = view;
        mView.setPresenter(this);
        mController = controller;
        mController.addListener(this);

        mUserMail = userMail;
    }

    @Override
    public void sendMessage(MessageVM message) {
        // TODO: remove down cast -> use Factory method
        TextMessageVM messageVM = (TextMessageVM)message;
        MessageFB newMessage = new MessageFB(mUserMail, messageVM.getText(), messageVM.getTime(), messageVM.isBookmarked(), MessageFB.TEXT_MSG, "", "");

        mController.sendMessage(newMessage);
    }

    @Override
    public void sendMedia(MessageVM messageVM) {
        // TODO: remove down cast -> use Factory method
        TextPhotoMessageVM photoMessageVM = (TextPhotoMessageVM) messageVM;
        MessageFB newMessage = new MessageFB(mUserMail, photoMessageVM.getText(), photoMessageVM.getTime(), photoMessageVM.isBookmarked(), MessageFB.PHOTO_MSG, photoMessageVM.getUriLocal(), "");

        mController.sendMedia(newMessage);
        mView.updateMessage(photoMessageVM);
    }

    @Override
    public void bookmarkMessage(MessageVM messageVM, int type) {
        // TODO: remove down cast -> use Factory method
        if(type == 0){
            TextMessageVM msgVM = (TextMessageVM) messageVM;
            MessageFB updateMessage = new MessageFB(mUserMail, msgVM.getText(), msgVM.getTime(), msgVM.isBookmarked(), MessageFB.PHOTO_MSG, "", "");
            updateMessage.setKey(msgVM.getKey());

            mController.updateMessage(updateMessage);
        }
        else if(type == 1)
        {
            TextPhotoMessageVM msgVM = (TextPhotoMessageVM) messageVM;
            MessageFB updateMessage = new MessageFB(mUserMail, msgVM.getText(), msgVM.getTime(), msgVM.isBookmarked(), MessageFB.PHOTO_MSG, msgVM.getUriLocal(), msgVM.getUriStorage());
            updateMessage.setKey(msgVM.getKey());

            mController.updateMessage(updateMessage);
        }

    }


    // Callback from Database

    @Override
    public void onChatChanged(ChatFB chat) {
        ChatVM chatVM = new ChatVM(chat.getKey(), chat.getTitle());
        mView.updateChatInfo(chatVM);
    }

    @Override
    public void onMessageAdded(MessageFB message) {
        MessageVM msgVM = null;
        if(message.getType()==MessageFB.TEXT_MSG){
            msgVM = createTextMessageVM(message);
        }
        else if(message.getType()==MessageFB.PHOTO_MSG)
        {
            msgVM = createTextPhotoMessageVM(message);
        }
        mView.updateMessage(msgVM);
    }

    @Override
    public void onMessageRemoved(MessageFB message) {
        MessageVM msgVM = null;
        if(message.getType()==MessageFB.TEXT_MSG){
            msgVM = createTextMessageVM(message);
        }
        else if(message.getType()==MessageFB.PHOTO_MSG)
        {
            msgVM = createTextPhotoMessageVM(message);
        }
        mView.removeMessage(msgVM);
    }

    @Override
    public void onMessageChanged(MessageFB message) {
        MessageVM msgVM = null;
        if(message.getType()==MessageFB.TEXT_MSG){
            msgVM = createTextMessageVM(message);
        }
        else if(message.getType()==MessageFB.PHOTO_MSG)
        {
            msgVM = createTextPhotoMessageVM(message);
        }
        mView.changeMessage(msgVM);
    }

    @Override
    public void onUploadPercent(MessageFB media, int perc){
        MessageVM messageVM = createTextPhotoMessageVM(media);
        mView.updatePercentUpload(messageVM, perc);
    }


    /**
     * Convert MessageFB to TextMessageVM
     * @param message
     * @return
     */
    private MessageVM createTextMessageVM(MessageFB message) {
        // Understand if the message is of Main User
        boolean who = MessageVM.THE_PARTNER;
        if (message.getEmail() != null) {   // TODO remove check in future
            if (message.getEmail().equals(mUserMail)) {
                who = MessageVM.THE_MAIN_USER;
            }
        }
        // Create respective ViewModel
        return new TextMessageVM(message.getText(), who, message.getDateTime(),
                message.isBookmarked(), message.getKey(), 0);
    }

    /**
     * Convert MessageFB to TextPhotoMessageVM
     * @param message
     * @return
     */
    private MessageVM createTextPhotoMessageVM(MessageFB message) {
        // Understand if the message is of Main User
        boolean who = MessageVM.THE_PARTNER;
        if (message.getEmail() != null) {   // TODO remove check in future
            if (message.getEmail().equals(mUserMail)) {
                who = MessageVM.THE_MAIN_USER;
            }
        }
        // Create respective ViewModel
        return new TextPhotoMessageVM(message.getText(), who, message.getDateTime(),
                message.isBookmarked(), message.getKey(), message.getUriLocal(), message.getUriStorage(), 0);
    }

}
