package com.sweetcompany.sweetie.geogift;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.DataMaker;
/**
 * Created by ghiro on 17/08/2017.
 */

public class GeogiftDoneFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GeogiftDoneContract.View{

    private static final String TAG = "GeogiftDoneFragment";

    private Toolbar mToolBar;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private TextView addressText;
    private TextView datetimePositionedText;
    private TextView isVisitedText;

    private GoogleApiClient googleApiClient;
    private Circle geoFenceLimits;
    private Marker geoFenceMarker;

    private Context mContext;
    private String titleGeogift;
    private float geofenceRadius = 100.0f; // in meters

    private GeoItem geoItem = null;
    double lat;
    double lon;
    LatLng coord;

    private GeogiftDoneContract.Presenter mPresenter;

    public static GeogiftDoneFragment newInstance(Bundle bundle) {
        GeogiftDoneFragment newGeogiftDoneFragment = new GeogiftDoneFragment();
        newGeogiftDoneFragment.setArguments(bundle);

        return newGeogiftDoneFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        //create GoogleApiClient
        createGoogleApi();
        //googleApiClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.geogift_done_fragment, container, false);

        // TODO: is useless to set titleGeogift, Firebase update it also if it is offline
        titleGeogift = getArguments().getString(GeogiftDoneActivity.GEOGIFT_TITLE);
        Log.d(TAG, "from Intent GEOGIFT_TITLE: " + titleGeogift);

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.geogift_done_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleGeogift);

        addressText = (TextView) root.findViewById(R.id.address_geogift_done_text);
        datetimePositionedText = (TextView) root.findViewById(R.id.datetime_geogift_done_text);
        isVisitedText = (TextView) root.findViewById(R.id.isvisited_geogift_done_text);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    // Initialize GoogleMaps
    private void initGMaps(){
        mapFragment = (SupportMapFragment) getChildFragmentManager() .findFragmentById(R.id.map_geogift_done_fragment);

        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // Callback called when Map is ready
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
        markerForGeofence(coord);
        centerMap(coord);
        drawGeofence();
    }

    private void centerMap(LatLng latLng){
        float zoom = 15f;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        map.moveCamera(cameraUpdate);
    }

    // Create a marker for the geofence creation
    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence("+latLng+")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if ( map!=null ) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = map.addMarker(markerOptions);
        }
    }

    // Draw Geofence circle on GoogleMap
    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");

        if ( geoFenceLimits != null )
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center( geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70,70,70))
                .fillColor( Color.argb(100, 150,150,150) )
                .radius( geofenceRadius );
        geoFenceLimits = map.addCircle( circleOptions );
    }

    private void removeGeofenceDraw() {
        Log.d(TAG, "removeGeofenceDraw()");
        if ( geoFenceMarker != null)
            geoFenceMarker.remove();
        if ( geoFenceLimits != null )
            geoFenceLimits.remove();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
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
    public void setPresenter(GeogiftDoneContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateGeogift(GeoItem geoitem) {
        Log.d(TAG, "updateGeogift");
        //mToolBar.setTitle(geoitem.getTytle);
        addressText.setText(getResources().getString(R.string.address_geogift)+" "+geoitem.getAddress());
        datetimePositionedText.setText(getResources().getString(R.string.datetime_positioned_geogift)+" "+ DataMaker.get_dd_MM_Local(geoitem.getDatetimeCreation()));
        if(geoitem.isVisited()){
            isVisitedText.setText(getResources().getString(R.string.isvisited_yes_geogifty));
            isVisitedText.setTextColor(Color.GREEN);
        }
        else{
            isVisitedText.setText(getResources().getString(R.string.isvisited_no_geogifty));
            isVisitedText.setTextColor(Color.RED);
        }
        lat = Double.parseDouble(geoitem.getLat());
        lon = Double.parseDouble(geoitem.getLon());
        coord = new LatLng(lat, lon);
        //TODO
        googleApiClient.connect();
    }
}
