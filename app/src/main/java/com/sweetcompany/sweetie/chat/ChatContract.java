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
        void updatePercentUpload(MessageVM mediaVM, int perc);

        boolean hideKeyboardPlaceholder();
    }

    interface Presenter {
        void sendTextMessage(MessageVM message);
        void sendPhotoMessage(MessageVM message);
        void bookmarkMessage(MessageVM message,int type);
    }
}
