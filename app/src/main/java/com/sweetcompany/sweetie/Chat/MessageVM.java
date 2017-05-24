package com.sweetcompany.sweetie.Chat;

import android.util.Log;
import android.view.View;

import com.sweetcompany.sweetie.Firebase.Message;

import java.util.Date;

/**
 * Created by Eduard on 18-May-17.
 */

abstract class MessageVM {
    static final boolean THE_MAIN_USER = true;
    static final boolean THE_PARTNER = false;

    private final String mKey;

    private final boolean mWho;
    private final String mDate;   // Format HH:mm
    private boolean mBookMarked;

    MessageVM(boolean who, String date, boolean bookMarked, String key) {
        mWho = who;
        mDate = date;
        mBookMarked = bookMarked;
        mKey = key;
    }

    boolean isTheMainUser() {
        return mWho;
    }
    boolean isThePartner() {
        return mWho;
    }

    String getDate() {
        return mDate;
    }

    boolean isBookmarked() {return mBookMarked; }
    void setBookmarked(boolean bookmarked) {mBookMarked = bookmarked; }

    String getKey() { return mKey; }

    abstract void configViewHolder(MessageViewHolder viewHolder);

    abstract int getIdView();

    abstract MessageViewHolder newViewHolder(View inflatedView);
}
