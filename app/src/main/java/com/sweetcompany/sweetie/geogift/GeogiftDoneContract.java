package com.sweetcompany.sweetie.geogift;


/**
 * Created by ghiro on 17/08/2017.
 */

interface GeogiftDoneContract {

    interface View {
        void setPresenter(GeogiftDoneContract.Presenter presenter);
        void updateGeogift(GeogiftVM geoitem);
    }

    interface Presenter {
    }
}