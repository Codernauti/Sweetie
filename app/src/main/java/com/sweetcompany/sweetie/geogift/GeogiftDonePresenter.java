package com.sweetcompany.sweetie.geogift;

import com.sweetcompany.sweetie.firebase.FirebaseGeogiftDoneController;
import com.sweetcompany.sweetie.model.GeogiftFB;

/**
 * Created by ghiro on 17/08/2017.
 */

public class GeogiftDonePresenter implements GeogiftDoneContract.Presenter, FirebaseGeogiftDoneController.GeogiftDoneControllerListener{

    public static final String TAG = "GeogiftDone.presenter" ;

    private final GeogiftDoneContract.View mView;
    private final FirebaseGeogiftDoneController mController;
    private String mUserUID;   // id of messages of main user

    public GeogiftDonePresenter(GeogiftDoneContract.View view, FirebaseGeogiftDoneController controller, String userMail) {
        mView = view;
        mView.setPresenter(this);
        mController = controller;
        mController.addListener(this);

        mUserUID = userMail;
    }

    @Override
    public GeoItem getGeoItem() {
        GeogiftFB geoGiftFB= null;
        geoGiftFB = mController.getGeoItemFB();
        GeoItem geoItem = createGeoItem(geoGiftFB);

        return geoItem;
    }
    /**
     * Convert GeogiftFB to GeoItem
     */
    private GeoItem createGeoItem(GeogiftFB geoItemFB) {
        GeoItem geoItemNew = null;
        geoItemNew.setType(geoItemFB.getmType());
        
        return geoItemNew;
    }
}