package com.codernauti.sweetie.geogift;


interface GeogiftDoneContract {

    interface View {
        void setPresenter(GeogiftDoneContract.Presenter presenter);
        void updateGeogift(GeogiftVM geoitem);
    }

    interface Presenter {
    }
}