package com.sweetcompany.sweetie.chatDiary;

import com.sweetcompany.sweetie.chat.MessageVM;

/**
 * Created by Eduard on 22-Aug-17.
 */

interface ChatDiaryContract {

    interface View {
        void setPresenter(ChatDiaryContract.Presenter presenter);

        void updateMessage(MessageVM msgVM);
        void removeMessage(String msgUid);
    }

    interface Presenter {

    }
}
