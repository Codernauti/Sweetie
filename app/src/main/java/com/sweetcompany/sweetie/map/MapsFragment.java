package com.sweetcompany.sweetie.map;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseMapController;

import java.util.List;
import java.util.Random;

public class MapsFragment extends Fragment implements MapContract.View,
                                                      OnMapReadyCallback,
                                                      GoogleApiClient.ConnectionCallbacks,
                                                      GoogleApiClient.OnConnectionFailedListener{


    private static final String TAG = "MapFragment";

    private View rootView;

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private Marker locationMarker;
    List<GalleryMapVM> galleriesList;

    private MapContract.Presenter mPresenter;
    private FirebaseMapController mMapController;

    private GoogleApiClient googleApiClient;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mActionAdapter = new ActionsAdapter();
        mContext = getContext();

        buildGoogleApiClient();
        googleApiClient.connect();
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.maps_fragment, container, false);
            // Initialise your layout here
        } else {
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
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_gallery_fragment);

        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        map = googleMap;
        //map.setOnMapClickListener(this);
        //map.setOnMarkerClickListener(this);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //map.setMyLocationEnabled(true);
        //map.setIndoorEnabled(true);
        //map.setBuildingsEnabled(true);
        //map.getUiSettings().setZoomControlsEnabled(true);
    }

    // Create a Location Marker
    private void markerLocations() {
        for(GalleryMapVM gallery : galleriesList){
            //TODO add icon and true location
            Random r = new Random();
            LatLng latLng = new LatLng((r.nextDouble() * -180.0) + 90.0,
                    (r.nextDouble() * -360.0) + 180.0);
            //LatLng latLng = new LatLng(Double.parseDouble(gallery.getmLat()) , Double.parseDouble(gallery.getmLon()));
            String title = gallery.getTitle();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            if ( map!=null ) {
                locationMarker = map.addMarker(markerOptions);
            }
        }
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

    }

    @Override
    public void removeGeogift(GeogiftMapVM geogift) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SupportMapFragment mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_gallery_fragment));
        if(mapFragment != null) {
            FragmentManager fM = getFragmentManager();
            fM.beginTransaction().remove(mapFragment).commit();
        }
    }

}