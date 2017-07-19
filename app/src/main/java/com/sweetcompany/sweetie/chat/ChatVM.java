package com.sweetcompany.sweetie.chat;

/**
 * Created by Eduard on 26/05/2017.
 */

class ChatVM {
    private String mKey;
    private String mTitle;

    ChatVM(String key, String title) {
        mKey = key;
        mTitle = title;
    }

    String getTitle() {
        return mTitle;
    }

}
