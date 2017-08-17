package com.sweetcompany.sweetie.geogift;

import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebaseGeogiftMakerController;
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.util.List;

/**
 * Created by ghiro on 16/08/2017.
 */

public class GeogiftMakerPresenter implements GeogiftMakerContract.Presenter, FirebaseGeogiftMakerController.GeogiftMakerControllerListener{

    public static final String TAG = "GeogiftMaker.presenter" ;

    private final GeogiftMakerContract.View mView;
    private final FirebaseGeogiftMakerController mController;
    private String mUserUID;   // id of messages of main user

    public GeogiftMakerPresenter(GeogiftMakerContract.View view, FirebaseGeogiftMakerController controller, String userMail) {
        mView = view;
        mView.setPresenter(this);
        mController = controller;
        mController.addListener(this);

        mUserUID = userMail;
    }

    @Override
    public List<String> pushGeogiftAction(GeoItem geoItem, String userInputGeogiftTitle, String username) {
        ActionFB action = null;
        // TODO: add description and fix username variable, what username???
        action = new ActionFB(userInputGeogiftTitle, mUserUID, username, "", DataMaker.get_UTC_DateTime(), ActionFB.GEOGIFT);

        if (action != null) {
            return mController.pushGeogiftAction(action, userInputGeogiftTitle, geoItem);
        }
        else {
            Log.d(TAG, "An error in the creation of a new GeogiftAction occurs!");
            return null;
        }
    }

    @Override
    public void uploadMedia(String uriImage) {
        mController.uploadMedia(uriImage);
    }

    @Override
    public void onMediaAdded(String uriStorage) {
        mView.setUriStorage(uriStorage);
    }

    @Override
    public void onUploadPercent(int perc) {
        mView.updatePercentUpload(perc);
    }
}