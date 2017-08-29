package com.sweetcompany.sweetie.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by ghiro on 25/07/2017.
 */

abstract class MediaViewHolder extends GalleryViewHolder {

    protected OnViewHolderClickListener mListener;

    interface OnViewHolderClickListener {
        void onPhotoClicked(int adapterPosition);
        void onPhotoLongClicked(int adapterPosition);
    }

    void setViewHolderClickListener(OnViewHolderClickListener listener) {
        mListener = listener;
    }

    void removeViewHolderClickListener() {
        mListener = null;
    }

    MediaViewHolder(View itemView) {
        super(itemView);
    }
}
