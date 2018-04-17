package com.codernauti.sweetie.gallery;

import android.view.View;

abstract class MediaViewHolder extends GalleryViewHolder {

    protected OnViewHolderClickListener mListener;

    interface OnViewHolderClickListener {
        void onPhotoClicked(int adapterPosition);
        void onPhotoLongClicked(int adapterPosition);
    }

    void setViewHolderClickListener(OnViewHolderClickListener listener) {
        mListener = listener;
    }

    MediaViewHolder(View itemView) {
        super(itemView);
    }

    abstract void setViewHolderSelected(boolean selected);
}
