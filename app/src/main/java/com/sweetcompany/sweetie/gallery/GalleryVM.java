package com.sweetcompany.sweetie.gallery;

/**
 * Created by ghiro on 22/07/2017.
 */

class GalleryVM {
    private String mKey;
    private String mTitle;

    GalleryVM(String key, String title) {
        mKey = key;
        mTitle = title;
    }

    String getTitle() {
        return mTitle;
    }

}
