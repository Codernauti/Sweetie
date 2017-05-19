package com.sweetcompany.sweetie.Chat;

/**
 * Created by ghiro on 16/05/2017.
 */

class TextMessageVM extends MessageVM {
    private String mText;

    TextMessageVM(String text, int who) {
        super(who);
        mText = text;
    }

    String getText() {
        return mText;
    }

    @Override
    void configViewHolder(ChatAdapter.TextMessageViewHolder viewHolder) {
        viewHolder.setText(mText);
    }
}

