package com.sweetcompany.sweetie.Chat;

import com.sweetcompany.sweetie.Firebase.FirebaseChatController;
import com.sweetcompany.sweetie.Firebase.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatPresenter implements ChatContract.Presenter, FirebaseChatController.OnFirebaseChatDataChange {

    ChatContract.View mView;
    FirebaseChatController mFirebaseController;

    public ChatPresenter(ChatContract.View view){
        mView = view;
        mView.setPresenter(this);
        mFirebaseController = FirebaseChatController.getInstance();
        mFirebaseController.addListener(this);
    }

    @Override
    public void start() {
        mFirebaseController.attachNetworkDatabase();
    }

    @Override
    public void pause() {
        mFirebaseController.detachNetworkDatabase();
    }

    @Override
    public void sendMessage(MessageVM message) {
        // TODO: remove down cast -> use Factory method
        TextMessageVM messageVM = (TextMessageVM)message;

        Message newMessage = new Message(messageVM.getText(), messageVM.getDate());
        mFirebaseController.pushMessage(newMessage);
    }

    @Override
    public void notifyNewMessages(List<Message> messages) {
        List<MessageVM> messagesVM = new ArrayList<>();
        for (Message msg : messages) {
            // TODO: convert user to boolean
            // TODO: check if the user of the message is the main user
            TextMessageVM msgVM = new TextMessageVM(msg.getText(), false, msg.getTime());
            messagesVM.add(msgVM);
        }

        mView.updateMessages(messagesVM);
    }
}
