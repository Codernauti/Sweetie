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
import java.util.List;

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

    private Location lastLocation;
    private static GoogleApiClient googleApiClient;
    private static PendingIntent geoFencePendingIntent;

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
                .setCircularRegion(geogiftFB.getLat(),
                                   geogiftFB.getLon(),
                        GEOFENCE_RADIUS
                )
                .setExpirationDuration( Geofence.NEVER_EXPIRE )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER )
                .build();

        //Debug pourpose
        Utility.addGeofenceToSharedPreference(this, geogiftFB.getKey());

        if(googleApiClient!= null && googleApiClient.isConnected()){
            addGeofencesOnLoad(geofence, geogiftFB.getKey(), geogiftFB.getKey());
        }
    }

    public void addGeofencesOnLoad(Geofence geofence, String actionKey, String geogiftKey) {
        LocationServices.GeofencingApi.addGeofences(
                googleApiClient,
                getGeofencingRequest(geofence),
                getGeofencePendingIntent(actionKey, geogiftKey)
        ).setResultCallback(this); // Result processed in onResult().
    }

    @Override
    public void onRemovedGeogift(String geogiftKey) {
        if(googleApiClient!= null && googleApiClient.isConnected()){
            unregisterGeofence(geogiftKey);
        }
    }

    public void unregisterGeofence(String geogiftKey){
        List<String> geofenceList = new ArrayList<>();
        geofenceList.add(geogiftKey);
             LocationServices.GeofencingApi.removeGeofences(
                    googleApiClient,
                     geofenceList).setResultCallback(this);
    }

    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        Log.d(TAG, "createGeofencingRequest");
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent(String actionKey, String geogiftKey) {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, GeofenceTrasitionService.class);
        intent.putExtra(GeofenceTrasitionService.GEOGIFT_ACTION_KEY, actionKey);
        intent.putExtra(GeofenceTrasitionService.GEOGIFT_KEY, geogiftKey);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
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
            Log.d(TAG, "permission ok... continuing");
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if ( lastLocation != null ) {
                //writeLastLocation();
                //startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
            }
        } else {
            Log.d(TAG, "Service cannot execute, it need the permission of ACCES_FINE_LOCATION");
            // TODO: show a notification to BaseActivity
        }
    }

    private boolean checkPermissionFineLocation() {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
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
