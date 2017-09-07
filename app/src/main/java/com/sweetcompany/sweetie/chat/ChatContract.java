package com.sweetcompany.sweetie.chat;

import java.util.List;

/**
 * Created by ghiro on 11/05/2017.
 */

interface ChatContract {

    interface View {
        void setPresenter(ChatContract.Presenter presenter);
        void updateChatInfo(ChatVM chat);

        void insertDateItem(DateItemVM dateVM);

        void updateMessage(MessageVM msgVM);
        void removeMessage(MessageVM msgVM);
        void changeMessage(MessageVM msgVM);
        void updatePercentUpload(String msgUid, int progress);

        boolean hideKeyboardPlaceholder();
    }

    interface Presenter {
        void sendTextMessage(String messageText);
        void sendPhotoMessage(String msgPhotoUriLocal, String msgText);
        void bookmarkMessage(MessageVM message,int type);
    }
}
