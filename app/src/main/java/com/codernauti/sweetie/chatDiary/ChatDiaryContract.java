package com.codernauti.sweetie.chatDiary;

import com.codernauti.sweetie.chat.MessageVM;


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
