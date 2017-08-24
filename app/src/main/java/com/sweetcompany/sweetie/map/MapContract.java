package com.sweetcompany.sweetie.map;

import java.util.List;

/**
 * Created by ghiro on 24/08/2017.
 */

public interface MapContract {

    interface View {
        void setPresenter(MapContract.Presenter presenter);
        void updateGalleryList(List<GalleryMapVM> galleriesVM);
    }

    interface Presenter {
        void DownloadGalleries();
    }

}