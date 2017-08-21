package com.sweetcompany.sweetie.chat;

import android.view.View;

/**
 * Created by Eduard on 18-May-17.
 */

abstract class MessageVM implements ChatItemVM {
    static final boolean THE_MAIN_USER = true;
    static final boolean THE_PARTNER = false;

    static final int TEXT_MSG = 0;
    static final int TEXT_PHOTO_MSG = 1;

    private String mKey;

    private final boolean mWho;
    private final String mTime;
    private boolean mBookMarked;
    private int percent;

    MessageVM(boolean who, String date, boolean bookMarked, String key, int perc) {
        mWho = who;
        mTime = date;
        mBookMarked = bookMarked;
        mKey = key;
        percent = perc;
    }

    boolean isTheMainUser() {
        return mWho;
    }

    String getTime() {
        return mTime;
    }

    boolean isBookmarked() {return mBookMarked; }
    void setBookmarked(boolean bookmarked) {mBookMarked = bookmarked; }

    @Override
    public String getKey() { return mKey; }
    void setKey(String key){
        this.mKey = key;
    }

    int getPercent(){
        return percent;
    }
    void setPercent(int perc){
        percent = perc;
    }

}
