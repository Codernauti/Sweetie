package com.sweetcompany.sweetie.chat;

import android.view.View;

/**
 * Created by Eduard on 18-May-17.
 */

abstract class MessageVM {
    static final boolean THE_MAIN_USER = true;
    static final boolean THE_PARTNER = false;

    private final String mKey;

    private final boolean mWho;
    private final String mTime;   // Format HH:mm
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

    String getKey() { return mKey; }

    int getPercent(){
        return percent;
    }
    void setPercent(int perc){
        percent = perc;
    }

    abstract void configViewHolder(MessageViewHolder viewHolder);

    abstract int getIdView();

    @Deprecated
    abstract MessageViewHolder newViewHolder(View inflatedView);
}
