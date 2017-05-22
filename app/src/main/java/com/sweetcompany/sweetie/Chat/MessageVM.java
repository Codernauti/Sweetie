package com.sweetcompany.sweetie.Chat;

import android.util.Log;
import android.view.View;

import java.util.Date;

/**
 * Created by Eduard on 18-May-17.
 */

abstract class MessageVM {
    static final int THE_PARTNER = 0;
    static final int THE_MAIN_USER = 1;

    static final boolean THE_MAIN_USER_BOOL = true;
    static final boolean THE_PARTNER_BOOL = false;

    private int mWho;
    private String mDate;   // Format HH:mm

    MessageVM(int who) {
        mWho = who;
    }

    MessageVM(int who, String date) {
        mWho = who;
        mDate = date;
    }

    boolean isTheMainUser() {
        return mWho == THE_MAIN_USER;
    }

    boolean isThePartner() {
        return mWho == THE_PARTNER;
    }

    String getDate() {
        return mDate;
    }

    abstract void configViewHolder(MessageViewHolder viewHolder);

    abstract int getIdView();

    abstract MessageViewHolder getViewHolder(View inflatedView);
}
