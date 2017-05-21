package com.sweetcompany.sweetie.Chat;

import android.view.View;

/**
 * Created by Eduard on 18-May-17.
 */

abstract class MessageVM {
    static final int THE_PARTNER = 0;
    static final int THE_MAIN_USER = 1;

    private int mWho;

    MessageVM(int who) {
        mWho = who;
    }

    boolean isTheMainUser() {
        return mWho == THE_MAIN_USER;
    }

    boolean isThePartner() {
        return mWho == THE_PARTNER;
    }

    abstract void configViewHolder(MessageViewHolder viewHolder);

    abstract int getIdView();

    abstract MessageViewHolder getViewHolder(View inflatedView);
}
