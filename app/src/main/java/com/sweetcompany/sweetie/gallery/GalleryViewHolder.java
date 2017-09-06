package com.sweetcompany.sweetie.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ghiro on 29/08/2017.
 */

abstract class GalleryViewHolder extends RecyclerView.ViewHolder {

    GalleryViewHolder(View itemView) {
        super(itemView);
    }

    abstract void setViewHolderSelected(boolean viewHolderSelected);
}
