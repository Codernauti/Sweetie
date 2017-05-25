package com.sweetcompany.sweetie.Chat;

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

    MessageVM(boolean who, String date, boolean bookMarked, String key) {
        mWho = who;
        mTime = date;
        mBookMarked = bookMarked;
        mKey = key;
    }

    boolean isTheMainUser() {
        return mWho;
    }
    boolean isThePartner() {
        return mWho;
    }

    String getTime() {
        return mTime;
    }

    boolean isBookmarked() {return mBookMarked; }
    void setBookmarked(boolean bookmarked) {mBookMarked = bookmarked; }

    String getKey() { return mKey; }

    abstract void configViewHolder(MessageViewHolder viewHolder);

    abstract int getIdView();

    abstract MessageViewHolder newViewHolder(View inflatedView);
}
