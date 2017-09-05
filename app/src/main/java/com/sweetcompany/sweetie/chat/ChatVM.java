package com.sweetcompany.sweetie.chat;

/**
 * Created by Eduard on 26/05/2017.
 */

class ChatVM {
    private String mKey;
    private String mTitle;
    private String mDate;
    private String mImageUri;

    ChatVM(String key, String title) {
        mKey = key;
        mTitle = title;
    }

    String getTitle() {
        return mTitle;
    }

    String getDate() {
        return mDate;
    }

    void setDate(String mDate) {
        this.mDate = mDate;
    }

    String getImageUri() {
        return mImageUri;
    }

    void setImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }
}
