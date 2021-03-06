package com.codernauti.sweetie.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract class GalleryViewHolder extends RecyclerView.ViewHolder {

    GalleryViewHolder(View itemView) {
        super(itemView);
    }

    abstract void setViewHolderSelected(boolean viewHolderSelected);
}
