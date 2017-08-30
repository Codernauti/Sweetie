package com.sweetcompany.sweetie;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.sweetcompany.sweetie.firebase.FirebaseGeogiftController;
import com.sweetcompany.sweetie.geogift.GeofenceTrasitionService;
import com.sweetcompany.sweetie.model.GeogiftFB;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

import java.util.ArrayList;

/**
 * Created by ghiro on 29/08/2017.
 */

public class GeogiftMonitorService extends Service implements ResultCallback<Status>,
                                                              GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
                                                              FirebaseGeogiftController.GeogiftControllerListener {

    private static final String TAG = "GeogiftMonitorService";

    private static final long GEO_DURATION = 10 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 100.0f; // in meters
    private static final int GEOFENCE_REQ_CODE = 4005;
    private int REQ_PERMISSION_UPDATE = 202;
    private static final int DWELL = 1000;

    private Location lastLocation;
    private static GoogleApiClient googleApiClient;
    private static PendingIntent geoFencePendingIntent;
    public ArrayList<String> mGeogiftKeyToRegister;

    private FirebaseGeogiftController mController = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mController = new FirebaseGeogiftController(Utility.getStringPreference(this, SharedPrefKeys.COUPLE_UID), Utility.getStringPreference(this, SharedPrefKeys.USER_UID));
        mController.setListener(this);
        mController.attachListenerDatabase();

        buildGoogleApiClient();
        googleApiClient.connect();
    }

    private void buildGoogleApiClient() {
        Log.d(TAG, "createGoogleApi()");
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        googleApiClient.disconnect();
    }

    //Callback Firebase
    @Override
    public void onAddedGeogift(GeogiftFB geogiftFB) {
        geoFencePendingIntent = null;

        Geofence geofence = new Geofence.Builder()
                .setRequestId(geogiftFB.getKey())
                .setCircularRegion(Double.parseDouble(geogiftFB.getLat()),
                        Double.parseDouble(geogiftFB.getLon()),
                        GEOFENCE_RADIUS
                )
                .setExpirationDuration( Geofence.NEVER_EXPIRE )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER )
                .build();

        //Debug pourpose
        Utility.addGeofenceToSharedPreference(this, geogiftFB.getKey());

        if(googleApiClient!= null && googleApiClient.isConnected()){
            addGeofencesOnLoad(geofence, geogiftFB.getActionKey());
        }
    }

    public void addGeofencesOnLoad(Geofence geofence, String actionKey) {
        try {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofencingRequest(geofence),
                    getGeofencePendingIntent(actionKey)
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            //logSecurityException(securityException);
        }
    }

    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        Log.d(TAG, "createGeofencingRequest");
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent(String actionKey) {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, GeofenceTrasitionService.class);
        intent.putExtra(GeofenceTrasitionService.GEOGIFT_ACTION_KEY, actionKey);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    @Override
    public void onRemovedGeogift(String geogiftKey) {

    }

    //ResultCallback
    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "geofence onResult: " + status);
    }

    //GoogleApiClient
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "googleApi connected");
        getLastKnownLocation();
    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");

        if ( checkPermissionFineLocation() ) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if ( lastLocation != null ) {
                //writeLastLocation();
                //startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
            }
        }
        else askPermission();
    }

    // Check for permission to access Location
    private boolean checkPermissionFineLocation() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        //TODO in activity
        /*ActivityCompat.requestPermissions(
                this,
                new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION_UPDATE
        );*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "googleApi connection suspended");
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "googleApi connection failed");
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
