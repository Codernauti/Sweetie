package com.sweetcompany.sweetie.map;

import com.sweetcompany.sweetie.firebase.FirebaseMapController;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.GeogiftFB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 24/08/2017.
 */

public class MapPresenter implements MapContract.Presenter, FirebaseMapController.MapGalleryControllerListener, FirebaseMapController.MapGeogiftControllerListener {

    private static final String TAG = "MapPresenter";

    private MapContract.View mView;
    private FirebaseMapController mController;

    public MapPresenter(MapContract.View view, FirebaseMapController controller){
        mView = view;
        mView.setPresenter(this);
        mController = controller;
        mController.addGalleryListener(this);
        mController.addGeogiftListener(this);
    }


    //TODO
    @Override
    public void onGalleryAdded(GalleryFB gallery) {

    }
    @Override
    public void onGalleryRemoved(GalleryFB gallery) {

    }
    @Override
    public void onGalleryChanged(GalleryFB gallery) {

    }


    @Override
    public void onGeogiftAdded(GeogiftFB geogift) {

    }
    @Override
    public void onGeogiftRemoved(GeogiftFB geogift) {

    }
}