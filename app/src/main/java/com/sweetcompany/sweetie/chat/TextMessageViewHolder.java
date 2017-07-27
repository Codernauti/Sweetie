package com.sweetcompany.sweetie.chat;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;

import io.github.rockerhieu.emojicon.EmojiconTextView;

/**
 * Created by Eduard on 21-May-17.
 */

class TextMessageViewHolder extends MessageViewHolder implements View.OnClickListener {
    private EmojiconTextView mTextMessage;
    private TextView mTextTime;
    private ImageButton mBookmarkButton;

    TextMessageViewHolder(View itemView, boolean isMainUser) {
        super(itemView);

        if (isMainUser) {
            mTextMessage = (EmojiconTextView) itemView.findViewById(R.id.chat_item_text_view);
            mTextTime = (TextView) itemView.findViewById(R.id.chat_item_time_text_view);
            mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_item_bookmark_button);
        }
        else {  // THE_PARTNER
            mTextMessage = (EmojiconTextView) itemView.findViewById(R.id.chat_partner_item_text_view);
            mTextTime = (TextView) itemView.findViewById(R.id.chat_partner_item_time_text_view);
            mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_partner_item_bookmark_button);
        }

        mBookmarkButton.setOnClickListener(this);
    }

    public void setText(String text) { mTextMessage.setText(text);}

    public void setTextTime(String time) {
        mTextTime.setText(time);
    }

    public void setBookmark(boolean isBookmarked) {
        mBookmarkButton.setSelected(isBookmarked);
    }

    @Override
    public void onClick(View v) {
        boolean wasBookmarked = mBookmarkButton.isSelected();
        mBookmarkButton.setSelected(!wasBookmarked);
        mListener.onBookmarkClicked(getAdapterPosition(), !wasBookmarked);
    }
}
