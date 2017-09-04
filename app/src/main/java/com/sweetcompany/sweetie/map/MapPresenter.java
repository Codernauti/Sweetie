package com.sweetcompany.sweetie.map;

import com.sweetcompany.sweetie.firebase.FirebaseMapController;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.GeogiftFB;

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
        GalleryMapVM newGalleryVM =  new GalleryMapVM(gallery.getKey(), gallery.getLatitude(), gallery.getLongitude(), gallery.getUriCover());
        mView.addGallery(newGalleryVM);
    }
    @Override
    public void onGalleryRemoved(GalleryFB gallery) {

    }
    @Override
    public void onGalleryChanged(GalleryFB gallery) {

    }


    @Override
    public void onGeogiftAdded(GeogiftFB geogift) {
        GeogiftMapVM newGeogiftMapVM = new GeogiftMapVM(geogift.getKey(), geogift.getLat(), geogift.getLon());
        newGeogiftMapVM.setType(geogift.getType());
        newGeogiftMapVM.setTitle(geogift.getTitle());
        mView.addGeogift(newGeogiftMapVM);
    }
    @Override
    public void onGeogiftRemoved(GeogiftFB geogift) {
        //TODO only key is needed?
        GeogiftMapVM removedGeogiftMapVM = new GeogiftMapVM(geogift.getKey(), geogift.getLat(), geogift.getLon());
        removedGeogiftMapVM.setType(geogift.getType());
        mView.removeGeogift(removedGeogiftMapVM);
    }
}