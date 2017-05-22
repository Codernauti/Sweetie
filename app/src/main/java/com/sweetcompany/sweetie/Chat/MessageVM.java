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

    private boolean mWho;
    private String mDate;   // Format HH:mm

    MessageVM(boolean who, String date) {
        mWho = who;
        mDate = date;
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

    abstract void configViewHolder(MessageViewHolder viewHolder);

    abstract int getIdView();

    abstract MessageViewHolder getViewHolder(View inflatedView);
}
