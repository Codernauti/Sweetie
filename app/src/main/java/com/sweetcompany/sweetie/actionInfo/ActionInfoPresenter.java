package com.sweetcompany.sweetie.actionInfo;

import android.net.Uri;

import com.sweetcompany.sweetie.firebase.FirebaseActionInfoController;

/**
 * Created by Eduard on 05-Sep-17.
 */

public class ActionInfoPresenter<AFB extends ActionInfoVM> implements ActionInfoContract.Presenter, FirebaseActionInfoController.Listener<AFB> {

    private static final String TAG = "GalleryInfoPresenter";

    private final ActionInfoContract.View mView;
    private final FirebaseActionInfoController<AFB> mController;


    public ActionInfoPresenter(ActionInfoContract.View view,
                         FirebaseActionInfoController<AFB> controller) {
        mController = controller;
        mController.setListener(this);

        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void changeTitle(String newTitle) {
        mController.changeTitle(newTitle);
    }

    @Override
    public void changeImage(Uri imageLocalUri) {
        mController.changeImage(imageLocalUri);
    }

    @Override
    public void changePosition(Double latitude, Double longitude) {
        mController.changePosition(latitude, longitude);
    }


    // Controller callback

    @Override
    public void onActionInfoChanged(AFB actionFB) {
        mView.updateInfo(actionFB);
    }
}
