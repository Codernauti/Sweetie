package com.sweetcompany.sweetie.chat;

/**
 * Created by Eduard on 18-May-17.
 */

public abstract class MessageVM implements ChatItemVM {
    static final boolean THE_MAIN_USER = true;
    static final boolean THE_PARTNER = false;

    static final int TEXT_MSG = 0;
    static final int TEXT_PHOTO_MSG = 1;

    private String mKey;

    private final boolean mWho;
    private final String mCreatorEmail;
    private final String mTime;
    private boolean mBookMarked;

    MessageVM(boolean who, String creatorEmail, String date, boolean bookMarked, String key) {
        mWho = who;
        mCreatorEmail = creatorEmail;
        mTime = date;
        mBookMarked = bookMarked;
        mKey = key;
    }

    boolean isTheMainUser() {
        return mWho;
    }

    String getCreatorEmail() {
        return mCreatorEmail;
    }

    public String getTime() {
        return mTime;
    }

    boolean isBookmarked() {return mBookMarked; }
    void setBookmarked(boolean bookmarked) {mBookMarked = bookmarked; }

    @Override
    public String getKey() { return mKey; }
    void setKey(String key){
        this.mKey = key;
    }

    int getPercent() { return 0; }
    void setPercent(int progress) {}

}
