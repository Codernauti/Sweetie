package com.sweetcompany.sweetie.chat;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.sweetcompany.sweetie.R;

/**
 * Created by Eduard on 21-May-17.
 */

abstract class MessageViewHolder extends ChatViewHolder implements View.OnClickListener,
        View.OnLongClickListener {

    private final ImageView mMsgStatus;
    private final ImageButton mBookmarkButton;

    private OnViewHolderClickListener mListener;

    interface OnViewHolderClickListener {
        void onBookmarkClicked(int adapterPosition, boolean isBookmarked, int type);
        void onPhotoClicked(int adapterPosition);
        void onMessageLongClicked(int adapterPosition);
        void onMessageClicked(int adapterPosition);
    }

    MessageViewHolder(View itemView) {
        super(itemView);
        // same view id for user/partner photo item and user/partner text item
        mMsgStatus = (ImageView) itemView.findViewById(R.id.chat_item_msg_state);
        mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_item_bookmark_button);

        mBookmarkButton.setOnClickListener(this);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    void setViewHolderClickListener(OnViewHolderClickListener listener) {
        mListener = listener;
    }

    void setSendingStatus() {
        mMsgStatus.setImageResource(R.drawable.ic_access_time_black_24dp);
    }
    void setSendStatus() {
        mMsgStatus.setImageResource(R.drawable.ic_check_black_24dp);
    }

    void setBookmark(boolean isBookmarked) {
        mBookmarkButton.setSelected(isBookmarked);
    }

    void enableBookmarkButton(){
        mBookmarkButton.setVisibility(View.VISIBLE);
    }

    void disableBookmarkButton() {
        mBookmarkButton.setVisibility(View.GONE);
    }

    abstract int getType();

    void photoClicked() {
        mListener.onPhotoClicked(getAdapterPosition());
    }

    void setViewHolderSelected(boolean selected) {
        if (selected) {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.chat_message_selected));
        } else {
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_item_bookmark_button: {
                boolean wasBookmarked = mBookmarkButton.isSelected();
                mBookmarkButton.setSelected(!wasBookmarked);
                mListener.onBookmarkClicked(getAdapterPosition(), !wasBookmarked, getType());
                break;
            }
            case R.id.chat_list_item_root: {
                mListener.onMessageClicked(getAdapterPosition());
                break;
            }
            default: {
                Log.d("MessageViewHolder", "onClick() event lost");
                break;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        mListener.onMessageLongClicked(getAdapterPosition());
        return true;
    }
}
