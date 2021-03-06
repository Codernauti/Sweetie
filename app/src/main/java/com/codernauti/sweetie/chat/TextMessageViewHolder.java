package com.codernauti.sweetie.chat;

import android.view.View;
import android.widget.TextView;

import com.codernauti.sweetie.R;

import io.github.rockerhieu.emojicon.EmojiconTextView;


class TextMessageViewHolder extends MessageViewHolder {
    private EmojiconTextView mTextMessage;
    private TextView mTextTime;
    /*private ImageButton mBookmarkButton;*/

    TextMessageViewHolder(View itemView) {
        super(itemView);

        mTextMessage = (EmojiconTextView) itemView.findViewById(R.id.chat_item_text_view);
        mTextTime = (TextView) itemView.findViewById(R.id.chat_item_time_text_view);
        /*mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_item_bookmark_button);
        mBookmarkButton.setOnClickListener(this);*/
    }

    public void setText(String text) { mTextMessage.setText(text);}

    public void setTextTime(String time) {
        mTextTime.setText(time);
    }

    /*public void setBookmark(boolean isBookmarked) {
        mBookmarkButton.setSelected(isBookmarked);
    }*/

    /*@Override
    public void onClick(View v) {
        boolean wasBookmarked = mBookmarkButton.isSelected();
        mBookmarkButton.setSelected(!wasBookmarked);
        mListener.onBookmarkClicked(getAdapterPosition(), !wasBookmarked, MessageVM.TEXT_MSG);
    }*/

    int getType() {
        return MessageVM.TEXT_MSG;
    }
}
