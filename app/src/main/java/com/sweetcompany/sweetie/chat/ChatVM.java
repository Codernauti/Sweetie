package com.sweetcompany.sweetie.chat;


class ChatVM {
    private String mKey;
    private String mTitle;
    private String mCreationDate;
    private String mImageUri;

    ChatVM(String key, String title) {
        mKey = key;
        mTitle = title;
    }

    String getTitle() {
        return mTitle;
    }

    String getCreationDate() {
        return mCreationDate;
    }
    void setCreationDate(String creationDate) {
        this.mCreationDate = creationDate;
    }

    String getImageUri() {
        return mImageUri;
    }
    void setImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }

}
