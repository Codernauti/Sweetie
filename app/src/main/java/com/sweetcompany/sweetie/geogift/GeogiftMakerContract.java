package com.sweetcompany.sweetie.geogift;

import java.util.List;

/**
 * Created by ghiro on 16/08/2017.
 */

interface GeogiftMakerContract {

    interface View {
        void setPresenter(GeogiftMakerContract.Presenter presenter);
        void updatePercentUpload(int perc);
        void setUriStorage(String uriStorage);
    }

    interface Presenter {
        List<String> pushGeogiftAction(String userInputGeogiftTitle, String username);
        void uploadMedia(String uriImage);
    }
}
