package com.sweetcompany.sweetie.Chat;

import com.sweetcompany.sweetie.Firebase.FirebaseChatController;
import com.sweetcompany.sweetie.Firebase.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatPresenter implements ChatContract.Presenter, FirebaseChatController.OnFirebaseChatDataChange {

    private ChatContract.View mView;
    private FirebaseChatController mFirebaseController;
    private String mUserMail;   // id of messages of main user

    public ChatPresenter(ChatContract.View view, String userMail){
        mView = view;
        mView.setPresenter(this);
        mFirebaseController = FirebaseChatController.getInstance();
        mUserMail = userMail;
    }

    @Override
    public void start() {
        mFirebaseController.attachNetworkDatabase();
        mFirebaseController.addListener(this);
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

        Message newMessage = new Message(mUserMail, messageVM.getText(), messageVM.getDate(), messageVM.isBookmarked());
        mFirebaseController.pushMessage(newMessage);
    }

    @Override
    public void notifyNewMessages(List<Message> messages) {
        List<MessageVM> messagesVM = new ArrayList<>();
        for (Message msg : messages) {
            boolean who = MessageVM.THE_PARTNER;

            // TODO: check if the user of the message is the main user
            if (msg.getEmail() != null) {   // TODO remove check in future
                if (msg.getEmail().equals(mUserMail)) {
                    who = MessageVM.THE_MAIN_USER;
                }
            }

            TextMessageVM msgVM = new TextMessageVM(msg.getText(), who, msg.getTime(),
                    msg.isBookmarked(), msg.getKey());
            messagesVM.add(msgVM);
        }

        mView.updateMessages(messagesVM);
    }

    @Override
    public void bookmarkMessage(MessageVM messageVM) {
        // TODO: remove down cast -> use Factory method
        TextMessageVM msgVM = (TextMessageVM) messageVM;
        Message updateMessage = new Message(mUserMail, msgVM.getText(), msgVM.getDate(), msgVM.isBookmarked());
        updateMessage.setKey(msgVM.getKey());

        mFirebaseController.updateMessage(updateMessage);
    }

}
