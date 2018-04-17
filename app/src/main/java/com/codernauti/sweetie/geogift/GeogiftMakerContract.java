package com.codernauti.sweetie.geogift;

import java.util.List;

interface GeogiftMakerContract {

    interface View {
        void setPresenter(GeogiftMakerContract.Presenter presenter);
        void updatePercentUpload(int perc);
        void setUriStorage(String uriStorage);
    }

    interface Presenter {
        List<String> pushGeogiftAction(GeogiftVM geoItem, String userInputGeogiftTitle, String userUid);
        void uploadMedia(String uriImage);
    }
}
