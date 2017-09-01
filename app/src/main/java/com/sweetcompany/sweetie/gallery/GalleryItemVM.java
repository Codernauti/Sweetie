package com.sweetcompany.sweetie.gallery;

/**
 * Created by ghiro on 29/08/2017.
 */

interface GalleryItemVM {

    void configViewHolder(GalleryViewHolder viewHolder);

    int getIdView();

    String getKey();

}
