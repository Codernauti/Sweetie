package com.sweetcompany.sweetie.chat;

import android.view.View;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.DataMaker;

/**
 * Created by ghiro on 16/05/2017.
 */

class TextMessageVM extends MessageVM {
    private String mText;

    TextMessageVM(String text, boolean mainUser, String date, boolean bookMarked, String key, int perc) {
        super(mainUser, date, bookMarked, key, perc);
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
        view.setTextTime(DataMaker.getHH_ss_Local(super.getTime()));
        view.setBookmark(super.isBookmarked());
    }

    @Override
    int getIdView() {
        if (isTheMainUser()) {
            return R.layout.chat_user_list_item_text;
        }
        else {  // isThePartner()
            return R.layout.chat_partner_list_item_text;
        }
    }

}

