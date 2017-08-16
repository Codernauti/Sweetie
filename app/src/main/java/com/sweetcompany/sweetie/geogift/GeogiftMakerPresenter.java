package com.sweetcompany.sweetie.geogift;

import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebaseGeogiftMakerController;
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.util.List;

/**
 * Created by ghiro on 16/08/2017.
 */

public class GeogiftMakerPresenter implements GeogiftMakerContract.Presenter{

    public static final String TAG = "Action.presenter" ;

    private final GeogiftMakerContract.View mView;
    private final FirebaseGeogiftMakerController mController;
    private String mUserMail;   // id of messages of main user

    public GeogiftMakerPresenter(GeogiftMakerContract.View view, FirebaseGeogiftMakerController controller, String userMail) {
        mView = view;
        mView.setPresenter(this);
        mController = controller;

        mUserMail = userMail;
    }

    @Override
    public List<String> pushGeogiftAction(String userInputGeogiftTitle, String username) {
        ActionFB action = null;
        // TODO: add description and fix username variable, what username???
        action = new ActionFB(userInputGeogiftTitle, username, "", DataMaker.get_UTC_DateTime(), ActionFB.GEOGIFT);

        if (action != null) {
            return mController.pushGeogiftAction(action, userInputGeogiftTitle);
        }
        else {
            Log.d(TAG, "An error in the creation of a new GeogiftAction occurs!");
            return null;
        }
    }
}