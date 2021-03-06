package com.codernauti.sweetie.chat;

import com.codernauti.sweetie.R;
import com.codernauti.sweetie.utils.DataMaker;

class TextMessageVM extends MessageVM {
    private String mText;

    TextMessageVM(String text, String creatorUserUid, boolean mainUser, String date, boolean bookMarked, String key) {
        super(mainUser, creatorUserUid, date, bookMarked, key);
        mText = text;
    }

    String getText() {
        return mText;
    }

    @Override
    public void configViewHolder(ChatViewHolder viewHolder) {
        // TODO: This downcast is secure?
        TextMessageViewHolder view = (TextMessageViewHolder) viewHolder;

        view.setText(mText);
        view.setTextTime(DataMaker.getHH_ss_Local(super.getTime()));
        view.setBookmark(super.isBookmarked());
    }

    @Override
    public int getIdView() {
        if (isTheMainUser()) {
            return R.layout.chat_user_list_item_text;
        }
        else {  // isThePartner()
            return R.layout.chat_partner_list_item_text;
        }
    }

}

