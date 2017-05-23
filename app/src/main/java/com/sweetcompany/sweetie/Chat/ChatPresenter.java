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

        Message newMessage = new Message(messageVM.getText(), messageVM.getDate(), messageVM.isBookmarked());
        mFirebaseController.pushMessage(newMessage);
    }

    @Override
    public void notifyNewMessages(List<Message> messages) {
        List<MessageVM> messagesVM = new ArrayList<>();
        for (Message msg : messages) {
            // TODO: check if the user of the message is the main user
            TextMessageVM msgVM = new TextMessageVM(msg.getText(), MessageVM.THE_PARTNER, msg.getTime(),
                    msg.isBookmarked(), msg.getKey());
            messagesVM.add(msgVM);
        }

        mView.updateMessages(messagesVM);
    }

    @Override
    public void bookmarkMessage(MessageVM messageVM) {
        // TODO: remove down cast -> use Factory method
        TextMessageVM msgVM = (TextMessageVM) messageVM;
        Message updateMessage = new Message(msgVM.getText(), msgVM.getDate(), msgVM.isBookmarked());
        updateMessage.setKey(msgVM.getKey());

        mFirebaseController.updateMessage(updateMessage);
    }

}
