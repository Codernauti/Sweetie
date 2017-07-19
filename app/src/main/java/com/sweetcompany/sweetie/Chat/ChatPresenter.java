package com.sweetcompany.sweetie.Chat;

import android.util.Log;

import com.sweetcompany.sweetie.Firebase.ChatFB;
import com.sweetcompany.sweetie.Firebase.FirebaseChatController;
import com.sweetcompany.sweetie.Firebase.MessageFB;

import java.util.List;

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
        MessageFB newMessage = new MessageFB(mUserMail, messageVM.getText(), messageVM.getTime(), messageVM.isBookmarked());

        mController.sendMessage(newMessage);
    }

    @Override
    public void bookmarkMessage(MessageVM messageVM) {
        // TODO: remove down cast -> use Factory method
        TextMessageVM msgVM = (TextMessageVM) messageVM;
        MessageFB updateMessage = new MessageFB(mUserMail, msgVM.getText(), msgVM.getTime(), msgVM.isBookmarked());
        updateMessage.setKey(msgVM.getKey());

        mController.updateMessage(updateMessage);
    }


    // Callback from Database

    @Override
    public void onChatChanged(ChatFB chat) {
        ChatVM chatVM = new ChatVM(chat.getKey(), chat.getTitle());
        mView.updateChatInfo(chatVM);
    }

    @Override
    public void onMessageAdded(MessageFB message) {
        MessageVM msgVM = createMessageVM(message);
        mView.updateMessage(msgVM);
    }

    @Override
    public void onMessageRemoved(MessageFB message) {
        MessageVM msgVM = createMessageVM(message);
        mView.removeMessage(msgVM);
    }

    @Override
    public void onMessageChanged(MessageFB message) {
        MessageVM msgVM = createMessageVM(message);
        mView.changeMessage(msgVM);
    }

    /**
     * Convert MessageFB to TextMessageVM
     * @param message
     * @return
     */
    private MessageVM createMessageVM(MessageFB message) {
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
    }

}
