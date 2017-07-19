package com.sweetcompany.sweetie.chat;

import java.util.List;

/**
 * Created by ghiro on 11/05/2017.
 */

interface ChatContract {

    interface View {
        void setPresenter(ChatContract.Presenter presenter);
        void updateMessages(List<MessageVM> messages);
        void updateChatInfo(ChatVM chat);

        void updateMessage(MessageVM msgVM);
        void removeMessage(MessageVM msgVM);
        void changeMessage(MessageVM msgVM);
    }

    interface Presenter {
        void sendMessage(MessageVM message);
        void bookmarkMessage(MessageVM message);
    }
}
