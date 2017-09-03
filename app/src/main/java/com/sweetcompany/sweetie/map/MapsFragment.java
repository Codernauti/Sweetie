package com.sweetcompany.sweetie.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseMapController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsFragment extends Fragment implements View.OnClickListener,
                                                      MapContract.View,
                                                      OnMapReadyCallback,
                                                      GoogleApiClient.ConnectionCallbacks,
                                                      GoogleApiClient.OnConnectionFailedListener{


    private static final String TAG = "MapFragment";
    private static final int GALLERY_MAP = 0;
    private static final int GEOGIFT_MAP = 1;

    private View rootView;
    private LinearLayout galleryButton;
    private LinearLayout geogiftButton;
    private TextView galleryButtonText;
    private TextView geogiftButtonText;

    private int currentSelectionMap = 1;

    private SupportMapFragment mapFragment;
    private GoogleMap map;

    private List<Marker> locationGeogiftMarkers = new ArrayList<>();
    private List<Marker> locationGalleryMarkers = new ArrayList<>();

    List<GalleryMapVM> mGalleriesList = new ArrayList<>();
    List<GeogiftMapVM> mGeogiftsList = new ArrayList<>();

    private MapContract.Presenter mPresenter;
    private FirebaseMapController mMapController;

    private GoogleApiClient googleApiClient;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mActionAdapter = new ActionsAdapter();
        mContext = getContext();
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            Log.d(TAG, "inflate rootView");
            rootView = inflater.inflate(R.layout.maps_fragment, container, false);

            galleryButton = (LinearLayout) rootView.findViewById(R.id.gallery_map_top_button);
            galleryButton.setClickable(true);
            galleryButton.setOnClickListener(this);
            geogiftButton = (LinearLayout) rootView.findViewById(R.id.geogift_map_top_button);
            geogiftButton.setClickable(true);
            geogiftButton.setOnClickListener(this);

            galleryButtonText = (TextView) rootView.findViewById(R.id.gallery_map_top_text);
            geogiftButtonText = (TextView) rootView.findViewById(R.id.geogift_map_top_text);

            //TODO refactor
            if(currentSelectionMap == GALLERY_MAP) {
                selectGalleryMap();
            }
            else{
                selectGeogiftMap();
            }

            buildGoogleApiClient();
            googleApiClient.connect();

        } else {
            Log.d(TAG, "remove rootView");
            container.removeView(rootView);
        }
        return rootView;
    }

    private void buildGoogleApiClient() {
        Log.d(TAG, "createGoogleApi()");
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    // Initialize GoogleMaps
    private void initGMaps(){
        Log.d(TAG, "initGMaps");
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_gallery_fragment);

        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        if(map == null) {
            map = googleMap;
            //map.setOnMapClickListener(this);
            //map.setOnMarkerClickListener(this);
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //map.setMyLocationEnabled(true);
            //map.setIndoorEnabled(true);
            //map.setBuildingsEnabled(true);
            //map.getUiSettings().setZoomControlsEnabled(true);
        }
        //TODO set smart bounds (have more marker on map)
    }

    // Create a Location Marker
    private void markerGeogift(GeogiftMapVM geogift) {

            LatLng latLng = new LatLng(Double.parseDouble(geogift.getLat()) , Double.parseDouble(geogift.getLon()));
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(geogift.getTitle())
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("action_gift_icon",72,72)));
            if ( map!=null ) {
                Marker geoMarker = map.addMarker(markerOptions);
                locationGeogiftMarkers.add(geoMarker);
                Log.d(TAG, "addMarker " + markerOptions.getTitle());
            }
    }

    private void markerGallery(GalleryMapVM gallery){
        Bitmap coverBmp = null;
        if(gallery.getUriCover() != null){
            try {
                coverBmp = Glide.
                        with(mContext).
                        load(gallery.getUriCover()).
                        asBitmap().
                        into(72, 72). // Width and height
                        get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        if(coverBmp != null){

            Bitmap markerIcon = Bitmap.createScaledBitmap(coverBmp, 72, 72, false);

            LatLng latLng = new LatLng(Double.parseDouble(gallery.getLat()) , Double.parseDouble(gallery.getLon()));
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(gallery.getTitle())
                    .icon(BitmapDescriptorFactory.fromBitmap(markerIcon));
            if ( map!=null ) {
                locationGalleryMarkers.add(map.addMarker(markerOptions));
                Log.d(TAG, "addMarker " + markerOptions.getTitle());
            }
        }
    }

    public int getIconDrawable(int type){
        int idDrawable = R.drawable.action_gift_icon;
        // TODO differentiate geogift types
        /*switch (type){
            case GeogiftFB.MESSAGE_GEOGIFT:
                idDrawable =  R.drawable.action_gift_icon;
        }*/

        return idDrawable;
    }

    public Bitmap resizeBitmap(String drawableName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(drawableName, "drawable", mContext.getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // initialize GoogleMaps
        initGMaps();
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    // TODO
    @Override
    public void addGallery(GalleryMapVM gallery) {

    }
    @Override
    public void removeGallery(GalleryMapVM gallery) {

    }
    @Override
    public void changeGallery(GalleryMapVM gallery) {

    }


    @Override
    public void addGeogift(GeogiftMapVM geogift) {
        mGeogiftsList.add(geogift);
        markerGeogift(geogift);
    }

    @Override
    public void removeGeogift(GeogiftMapVM geogift) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(map!=null){
            map.clear();
            mGeogiftsList.clear();
            mGalleriesList.clear();

            //TODO refactor
            if(currentSelectionMap == GALLERY_MAP) {
                selectGalleryMap();
            }
            else{
                selectGeogiftMap();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
        SupportMapFragment mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_gallery_fragment));
        if(mapFragment != null) {
            FragmentManager fM = getFragmentManager();
            fM.beginTransaction().remove(mapFragment).commit();
        }
    }

    public void selectGalleryMap(){
        currentSelectionMap = GALLERY_MAP;
        galleryButtonText.setTextColor(Color.YELLOW);
        geogiftButtonText.setTextColor(Color.GRAY);
        for(Marker markerGeo : locationGeogiftMarkers) markerGeo.setVisible(false);
        for(Marker markerGall : locationGalleryMarkers) markerGall.setVisible(true);
    }

    public void selectGeogiftMap(){
        currentSelectionMap = GEOGIFT_MAP;
        geogiftButtonText.setTextColor(Color.YELLOW);
        galleryButtonText.setTextColor(Color.GRAY);
        for(Marker markerGall : locationGalleryMarkers) markerGall.setVisible(false);
        for(Marker markerGeo : locationGeogiftMarkers) markerGeo.setVisible(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallery_map_top_button: {
                selectGalleryMap();
                break;
            }
            case R.id.geogift_map_top_button: {
                selectGeogiftMap();
                break;
            }
            default:
                break;
        }
    }
}