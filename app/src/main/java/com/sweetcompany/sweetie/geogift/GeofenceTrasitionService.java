package com.sweetcompany.sweetie.geogift;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.sweetcompany.sweetie.MainActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseGeogiftController;
import com.sweetcompany.sweetie.model.GeogiftFB;
import com.sweetcompany.sweetie.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 10/08/2017.
 */

public class GeofenceTrasitionService extends IntentService implements ResultCallback<Status>,
                                                                       GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
                                                                       FirebaseGeogiftController.GeogiftControllerListener{

    private static final String TAG = "GeofenceTrasitionServ";

    public static final int GEOFENCE_NOTIFICATION_ID = 0;

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


    public GeofenceTrasitionService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

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

        googleApiClient.disconnect();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        // Handling errors
        if ( geofencingEvent.hasError() ) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode() );
            Log.e( TAG, errorMsg );
            return;
        }

        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type is of interest
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT  || geoFenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            Log.d(TAG, "geofence triggered!");
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            String geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences );

            // Send notification details as a String
            sendNotification( geofenceTransitionDetails );
        }
    }

    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            triggeringGeofencesList.add( geofence.getRequestId() );
        }

        String status = null;
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER )
            status = "Entering ";
        else if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT )
            status = "Exiting ";
        else if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL )
            status = "Dwell ";
        return status + TextUtils.join( ", ", triggeringGeofencesList);
    }

    private void sendNotification( String msg ) {
        Log.i(TAG, "sendNotification: " + msg );

        // Intent to start the main Activity
        Intent notificationIntent = MainActivity.makeNotificationIntent(
                getApplicationContext(), msg
        );

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        // Creating and sending Notification
        NotificationManager notificatioMng =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificatioMng.notify(
                GEOFENCE_NOTIFICATION_ID,
                createNotification(msg, notificationPendingIntent));

    }

    //Callback Firebase
    @Override
    public void onAddedGeogift(GeogiftFB geogiftFB) {
        geoFencePendingIntent = null;

        Geofence geofence = new Geofence.Builder()
                .setRequestId(geogiftFB.getAddress())
                .setCircularRegion(Double.parseDouble(geogiftFB.getLat()),
                        Double.parseDouble(geogiftFB.getLon()),
                        GEOFENCE_RADIUS
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(DWELL)
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT  | Geofence.GEOFENCE_TRANSITION_DWELL)
                .build();

        Utility.addGeofenceToSharedPreference(this, geogiftFB.getKey());

        if(googleApiClient!= null && googleApiClient.isConnected()){
            addGeofencesOnLoad(geofence);
        }
    }

    public void addGeofencesOnLoad(Geofence geofence) {
        try {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofencingRequest(geofence),
                    getGeofencePendingIntent()
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

    private PendingIntent getGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    @Override
    public void onRemovedGeogift(String geogiftKey) {

    }


    // Create notification
    private Notification createNotification(String msg, PendingIntent notificationPendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
                .setSmallIcon(R.drawable.action_gift_icon)
                .setColor(Color.RED)
                .setContentTitle(msg)
                .setContentText("Geofence Notification!")
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notificationBuilder.build();
    }

    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
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


}
