package com.sweetcompany.sweetie.gallery;

import android.net.Uri;

import com.sweetcompany.sweetie.firebase.FirebaseActionInfoController;
import com.sweetcompany.sweetie.model.GalleryFB;

/**
 * Created by Eduard on 04-Sep-17.
 */

class GalleryInfoPresenter implements GalleryInfoContract.Presenter,
        FirebaseActionInfoController.Listener<GalleryFB> {

    private static final String TAG = "GalleryInfoPresenter";

    private final GalleryInfoContract.View mView;
    private final FirebaseActionInfoController<GalleryFB> mController;


    GalleryInfoPresenter(GalleryInfoContract.View view,
                        FirebaseActionInfoController<GalleryFB> controller) {
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
    public void onActionInfoChanged(GalleryFB actionFB) {
        GalleryVM gallery = new GalleryVM(actionFB.getKey(), actionFB.getTitle(), actionFB.getCreationDate(),
                actionFB.getUriCover(), actionFB.getLatitude(), actionFB.getLongitude());

        mView.updateInfo(gallery);
    }

}
