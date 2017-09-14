package com.sweetcompany.sweetie.chatDiary;

import com.sweetcompany.sweetie.chat.MessageVM;


interface ChatDiaryContract {

    interface View {
        void setPresenter(ChatDiaryContract.Presenter presenter);

        void updateMessage(MessageVM msgVM);
        void removeMessage(String msgUid);
    }

    interface Presenter {
        void removeBookmarkedMessage(MessageVM message);
    }
}
