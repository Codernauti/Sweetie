package com.sweetcompany.sweetie.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Eduard on 21-May-17.
 */

abstract class MessageViewHolder extends RecyclerView.ViewHolder {

    interface OnViewHolderClickListener {
        void onBookmarkClicked(int adapterPosition);
    }

    protected OnViewHolderClickListener mListener;

    void setViewHolderClickListener(OnViewHolderClickListener listener) {
        mListener = listener;
    }

    void removeViewHolderClickListener() {
        mListener = null;
    }

    MessageViewHolder(View itemView) {
        super(itemView);
    }
}
