package com.sweetcompany.sweetie.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sweetcompany.sweetie.R;

/**
 * Created by Eduard on 21-May-17.
 */

abstract class MessageViewHolder extends RecyclerView.ViewHolder {

    private final ImageView mMsgStatus;

    protected OnViewHolderClickListener mListener;

    interface OnViewHolderClickListener {
        void onBookmarkClicked(int adapterPosition, boolean isBookmarked, int type);
        void onPhotoClicked(int adapterPosition);
    }

    MessageViewHolder(View itemView) {
        super(itemView);
        mMsgStatus = (ImageView) itemView.findViewById(R.id.chat_item_msg_state);
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

}
