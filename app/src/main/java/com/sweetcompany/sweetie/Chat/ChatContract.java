package com.sweetcompany.sweetie.Chat;

import java.util.List;

/**
 * Created by ghiro on 11/05/2017.
 */

public interface ChatContract {

    interface View {
        void setPresenter(ChatContract.Presenter presenter);
        void updateMessages(List<MessageVM> messages);
        void updateChatInfo(ChatVM chat);
    }

    interface Presenter {
        void start();
        void pause();
        void sendMessage(MessageVM message);
        void bookmarkMessage(MessageVM message);
    }
}
