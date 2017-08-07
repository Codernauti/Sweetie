package com.sweetcompany.sweetie.geogift;

import com.sweetcompany.sweetie.firebase.FirebaseGeogiftController;

/**
 * Created by ghiro on 07/08/2017.
 */

public class GeogiftPresenter implements GeogiftContract.Presenter, FirebaseGeogiftController.GeogiftControllerListener {

    private static final String TAG = "GeogiftPresenter";

    private final GeogiftContract.View mView;
    private final FirebaseGeogiftController mController;
    private final String mUserMail;   // id of messages of main user

    GeogiftPresenter(GeogiftContract.View view, FirebaseGeogiftController controller, String userMail){
        mView = view;
        mView.setPresenter(this);
        mController = controller;
        //mController.addListener(this);

        mUserMail = userMail;
    }

}
