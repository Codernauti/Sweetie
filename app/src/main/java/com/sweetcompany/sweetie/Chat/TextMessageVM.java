package com.sweetcompany.sweetie.Chat;

import android.view.View;

import com.sweetcompany.sweetie.R;

/**
 * Created by ghiro on 16/05/2017.
 */

class TextMessageVM extends MessageVM {
    private String mText;

    TextMessageVM(String text, boolean mainUser, String date) {
        super(mainUser, date);
        mText = text;
    }

    String getText() {
        return mText;
    }

    @Override
    void configViewHolder(MessageViewHolder viewHolder) {
        // TODO: This downcast is secure?
        TextMessageViewHolder view = (TextMessageViewHolder) viewHolder;
        view.setText(mText);
        view.setTextTime(super.getDate());
    }

    @Override
    int getIdView() {
        if (isTheMainUser()) {
            return R.layout.chat_user_list_item;
        }
        else {  // isThePartner()
            return R.layout.chat_partner_list_item;
        }
    }

    @Override
    TextMessageViewHolder getViewHolder(View inflatedView) {
        return new TextMessageViewHolder(inflatedView, isTheMainUser());
    }
}

