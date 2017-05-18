package com.sweetcompany.sweetie.Chat;

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

    int getWho() {
        return mWho;
    }

    abstract void configViewHolder(ChatAdapter.TextMessageViewHolder viewHolder);
}
