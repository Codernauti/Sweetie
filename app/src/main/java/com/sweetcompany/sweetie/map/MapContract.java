package com.sweetcompany.sweetie.map;

public interface MapContract {

    interface View {
        void setPresenter(MapContract.Presenter presenter);
        void addGallery(GalleryMapVM gallery);
        void removeGallery(String galleryKey);
        void changeGallery(GalleryMapVM gallery);

        void addGeogift(GeogiftMapVM geogift);
        void removeGeogift(String geogiftkey);
    }

    interface Presenter {
    }

}