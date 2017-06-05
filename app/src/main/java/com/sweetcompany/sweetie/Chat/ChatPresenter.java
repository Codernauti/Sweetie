package com.sweetcompany.sweetie.Chat;

import android.util.Log;

import com.sweetcompany.sweetie.Firebase.ChatFB;
import com.sweetcompany.sweetie.Firebase.FirebaseChatController;
import com.sweetcompany.sweetie.Firebase.MessageFB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 11/05/2017.
 */

class ChatPresenter implements ChatContract.Presenter, FirebaseChatController.OnFirebaseChatDataChange {

    private static final String TAG = "ChatPresenter";

    private ChatContract.View mView;
    private FirebaseChatController mFirebaseController;
    private String mUserMail;   // id of messages of main user
    private String mChatKey;    // id of chat opened
    private String mActionKey;  // id of actionChat references

    ChatPresenter(ChatContract.View view, String userMail, String chatKey, String actionKey){
        mView = view;
        mView.setPresenter(this);
        mFirebaseController = FirebaseChatController.getInstance();
        mUserMail = userMail;
        mChatKey = chatKey;
        mActionKey = actionKey;
    }

    @Override
    public void start() {
        if (mChatKey != null) {
            mFirebaseController.attachNetworkDatabase(mChatKey);
            mFirebaseController.addListener(this);
        }
        else {
            Log.w(TAG, "start(): impossible attach to database because mChatKey is NULL");
        }
    }

    @Override
    public void pause() {
        mFirebaseController.detachNetworkDatabase();
        mFirebaseController.removeListener(this);
    }

    @Override
    public void sendMessage(MessageVM message) {
        // TODO: remove down cast -> use Factory method
        TextMessageVM messageVM = (TextMessageVM)message;
        MessageFB newMessage = new MessageFB(mUserMail, messageVM.getText(), messageVM.getTime(), messageVM.isBookmarked());

        mFirebaseController.sendMessage(newMessage, mActionKey);
    }

    @Override
    public void bookmarkMessage(MessageVM messageVM) {
        // TODO: remove down cast -> use Factory method
        TextMessageVM msgVM = (TextMessageVM) messageVM;
        MessageFB updateMessage = new MessageFB(mUserMail, msgVM.getText(), msgVM.getTime(), msgVM.isBookmarked());
        updateMessage.setKey(msgVM.getKey());

        mFirebaseController.updateMessage(updateMessage);
    }


    // Callback from Database

    @Override
    public void notifyChats(List<ChatFB> chats) {
        ChatVM chatVM = new ChatVM("", "");
        for (ChatFB chat : chats) {
            // search for the exact chat opened
            if (chat.getKey().equals(mChatKey)) {
                chatVM = new ChatVM(chat.getKey(), chat.getTitle());
            }
            else {
                Log.w(TAG, "notifyChats(): can't find Chat in database");
            }
        }
        mView.updateChatInfo(chatVM);
    }

    @Override
    public void notifyNewMessage(MessageFB message) {
        MessageVM msgVM = createMessageVM(message);
        mView.updateMessage(msgVM);
    }

    @Override
    public void notifyRemovedMessage(MessageFB message) {
        MessageVM msgVM = createMessageVM(message);
        mView.removeMessage(msgVM);
    }

    @Override
    public void notifyChangedMessage(MessageFB message) {
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
