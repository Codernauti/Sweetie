package com.sweetcompany.sweetie.Chat;

/**
 * Created by ghiro on 16/05/2017.
 */

class TextMessageVM {

    static final int THE_USER = 0;
    static final int THE_PARTNER = 1;

    private String mText;
    private boolean mMe; //is me?

    public TextMessageVM(String text, boolean me) {
        mText = text;
        mMe = me;
    }

    public String getText() {
        return mText;
    }

    public int whoIs() {
        // TODO: usare un booleano è corretto???
        return mMe? THE_USER : THE_PARTNER;
    }
}

