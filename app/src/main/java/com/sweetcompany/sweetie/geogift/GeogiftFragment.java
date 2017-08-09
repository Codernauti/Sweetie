package com.sweetcompany.sweetie.geogift;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.sweetcompany.sweetie.R;

import java.util.concurrent.Executor;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ghiro on 07/08/2017.
 */

public class GeogiftFragment  extends Fragment implements GeogiftContract.View, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "GeogiftFragment";

    private static final int PLACE_PICKER_REQUEST = 1;
    private int REQ_PERMISSION_UPDATE = 202;

    //Get the desired interval of this request, in milliseconds.
    private final int UPDATE_INTERVAL =  8 * 1000; // 30 secs
    //The system will never provide location updates faster than the minimum of getFastestInterval() and getInterval()
    private final int FASTEST_INTERVAL = 5 * 1000;  // 5 secs

    private Toolbar mToolBar;
    private TextView coordText;
    private Button pickPositionButton;
    private TextView googleApiConnectionText;
    private TextView locationConnectionText;
    private TextView currentText;

    private LatLngBounds latLngBounds = null;
    private LatLng latLng = null;
    private boolean mLocationPermissionGranted = false;

    private Location lastLocation;
    private LocationRequest locationRequest;

    private FusedLocationProviderClient mFusedLocationClient;

    private GeofencingClient mGeofencingClient;
    private GoogleApiClient googleApiClient;

    private GeogiftContract.Presenter mPresenter;

    public static GeogiftFragment newInstance(Bundle bundle) {
        GeogiftFragment newGeogiftFragment = new GeogiftFragment();
        newGeogiftFragment.setArguments(bundle);

        return newGeogiftFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGeofencingClient = LocationServices.getGeofencingClient(getActivity());
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //create GoogleApiClient
        createGoogleApi();
        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // root is a RelativeLayout
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.geogift_fragment, container, false);

        // TODO: is useless to set titleChat, Firebase update it also if it is offline
        String titleGeogift = getArguments().getString(GeogiftActivity.GEOGIFT_TITLE);
        String geogiftUid = getArguments().getString(GeogiftActivity.GEOGIFT_DATABASE_KEY);
        Log.d(TAG, "from Intent GEOGIFT_TITLE: " + titleGeogift);
        Log.d(TAG, "from Intent GEOGIFT_DATABASE_KEY: " + geogiftUid);

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.geogift_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleGeogift);

        coordText = (TextView) root.findViewById(R.id.coord_text_view);
        pickPositionButton = (Button) root.findViewById(R.id.pick_location_button);
        googleApiConnectionText = (TextView) root.findViewById(R.id.connection_api_text_view);
        locationConnectionText = (TextView) root.findViewById(R.id.location_api_text_view);
        currentText = (TextView) root.findViewById(R.id.current_api_text_view);

        pickPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()) {
                    pickPosition();
                }
                else askPermission();
            }
        });

        return root;
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

    public void pickPosition(){
        latLngBounds = new LatLngBounds(new LatLng(44.882494, 11.601847), new LatLng(44.909004, 11.613520));
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            builder.setLatLngBounds(latLngBounds);
            Intent i = builder.build(getActivity());
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(getContext(), data);
            String address;
            LatLng latLng;
            String name;

            if (place == null) {
                Log.i(TAG, "No place selected");
                return;
            }else
            {
                name = place.getName().toString();
                address = place.getAddress().toString();
                latLng = place.getLatLng();
            }

            coordText.setText(name+"\n"+
                              address+"\n"+
                              latLng.latitude+"\n"+
                              latLng.longitude);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }

    @Override
    public void setPresenter(GeogiftContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        googleApiConnectionText.setText("Connected");
        getLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
        googleApiConnectionText.setText("Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
        googleApiConnectionText.setText("Connection failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged ["+location+"]");
        currentText.setText("onLocationChanged ["+location+"]");
        lastLocation = location;
        writeActualLocation(location);
    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        locationConnectionText.setText("getLastKnownLocation()");

        if ( checkPermission() ) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if ( lastLocation != null ) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                locationConnectionText.setText("LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());

                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                locationConnectionText.setText("No location retrieved yet");
                startLocationUpdates();
            }
        }
        else askPermission();
    }

    // Start location Updates
    private void startLocationUpdates(){
        Log.i(TAG, "startLocationUpdates()");
        locationConnectionText.setText("startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) //Set the priority of the request
                .setInterval(UPDATE_INTERVAL) //Set the desired interval for active location updates
                .setFastestInterval(FASTEST_INTERVAL); //Explicitly set the fastest interval for location updates

        if (checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }

    // Write location coordinates on UI
    private void writeActualLocation(Location location) {
        //textLat.setText( "Lat: " + location.getLatitude() );
        //textLong.setText( "Long: " + location.getLongitude() );
        currentText.setText("Lat: " + location.getLatitude());
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION_UPDATE
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case 202: //REQ_PERMISSION
                {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
    }

}
