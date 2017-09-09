package com.sweetcompany.sweetie.map;

/**
 * Created by ghiro on 24/08/2017.
 */

public interface MapContract {

    interface View {
        void setPresenter(MapContract.Presenter presenter);
        void addGallery(GalleryMapVM gallery);
        void removeGallery(GalleryMapVM gallery);
        void changeGallery(GalleryMapVM gallery);

        void addGeogift(GeogiftMapVM geogift);
        void removeGeogift(GeogiftMapVM geogift);
    }

    interface Presenter {
    }

}