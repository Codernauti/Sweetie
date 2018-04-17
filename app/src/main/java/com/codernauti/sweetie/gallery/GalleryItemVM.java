package com.codernauti.sweetie.gallery;

interface GalleryItemVM {

    void configViewHolder(GalleryViewHolder viewHolder);

    int getIdView();

    String getKey();

}
