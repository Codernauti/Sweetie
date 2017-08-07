package com.sweetcompany.sweetie.geogift;

/**
 * Created by ghiro on 07/08/2017.
 */

interface GeogiftContract {

    interface View {
        void setPresenter(GeogiftContract.Presenter presenter);
        //void updateChatInfo(GeogiftVM chat);
    }

    interface Presenter {
        //void sendTextMessage(MessageVM message);
    }
}
