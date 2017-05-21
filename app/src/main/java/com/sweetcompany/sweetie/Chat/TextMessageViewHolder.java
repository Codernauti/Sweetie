package com.sweetcompany.sweetie.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;

/**
 * Created by Eduard on 21-May-17.
 */

class TextMessageViewHolder extends MessageViewHolder implements View.OnClickListener {
    private TextView mTextMessage;
    private ImageButton mBookmarkButton;

    TextMessageViewHolder(View itemView, boolean isMainUser) {
        super(itemView);

        if (isMainUser) {
            mTextMessage = (TextView) itemView.findViewById(R.id.chat_item_text_view);
            mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_item_bookmark_button);
        }
        else {  // THE_PARTNER
            mTextMessage = (TextView) itemView.findViewById(R.id.chat_partner_item_text_view);
            mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_partner_item_bookmark_button);
        }

        mBookmarkButton.setOnClickListener(this);
    }

    public void setText(String text) { mTextMessage.setText(text);}

    @Override
    public void onClick(View v) {
        mBookmarkButton.setSelected(!mBookmarkButton.isSelected());
    }
}
