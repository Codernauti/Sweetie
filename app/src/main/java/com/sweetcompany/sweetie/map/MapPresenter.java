package com.sweetcompany.sweetie.map;

import com.sweetcompany.sweetie.firebase.FirebaseMapController;
import com.sweetcompany.sweetie.model.GalleryFB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 24/08/2017.
 */

public class MapPresenter implements MapContract.Presenter, FirebaseMapController.MapControllerListener {

    private static final String TAG = "MapPresenter";

    private MapContract.View mView;
    private FirebaseMapController mController;

    private ArrayList<GalleryMapVM> mGalleryList = new ArrayList<>();

    public MapPresenter(MapContract.View view, FirebaseMapController controller){
        mView = view;
        mView.setPresenter(this);
        mController = controller;
        mController.addListener(this);
    }

    // Clear actions, retrieve all actions on server
    @Override
    public void updateGalleryList(List<GalleryFB> galleriesFB) {
        GalleryMapVM newActionVM;
        mGalleryList.clear();

        for(GalleryFB galleryFB : galleriesFB){
          newActionVM = new GalleryMapVM(galleryFB.getKey(), galleryFB.getTitle());
          newActionVM.setmLat(galleryFB.getLatitude());
          newActionVM.setmLon(galleryFB.getLongitude());
          newActionVM.setmUriCover(galleryFB.getUriCover());

          mGalleryList.add(newActionVM);
        }

        mView.updateGalleryList(mGalleryList);
    }

    @Override
    public void DownloadGalleries() {
        mController.attachNetworkDatabase();
    }
}