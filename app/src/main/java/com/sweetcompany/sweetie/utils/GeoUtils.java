package com.sweetcompany.sweetie.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.geogift.GeofenceTrasitionService;
import com.sweetcompany.sweetie.model.GeogiftFB;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by ghiro on 12/08/2017.
 */


public class GeoUtils implements ResultCallback<Status>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "GeoUtils";
    static String GEOGIFT = "geogifts";
    private static final long GEO_DURATION = 10 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 100.0f; // in meters
    private static final int GEOFENCE_REQ_CODE = 4005;
    private static final int DWELL = 1000;

    private MiniPrefDB miniPrefDB;
    Set<String> listGeogift;

    private static DatabaseReference mGeogiftDbReference;
    public static Context mContext;
    public static String userID;
    public ArrayList<Geofence> mGeofenceList;

    private static GoogleApiClient googleApiClient;
    private static PendingIntent geoFencePendingIntent;

    public GeoUtils(Context appContext) {
        mContext = appContext;

        mGeogiftDbReference = FirebaseDatabase.getInstance()
                .getReference(GEOGIFT + "/" + Utility.getStringPreference(mContext, Utility.COUPLE_UID));
        userID = Utility.getStringPreference(mContext, Utility.USER_UID);

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();

        geoFencePendingIntent = null;

        miniPrefDB = new MiniPrefDB(mContext);
        //listGeogift = miniPrefDB.getGeogiftSet();
    }

    // Check for permission to access Location
    static public boolean checkPermissionAccessFineLocation(Context context) {
        Log.d(TAG, "checkPermissionAccesFineLocation()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }


    public void registerGeofences(){
        retrieveGeogift();
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "createGoogleApi()");
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        googleApiClient.connect();
    }

    private void retrieveGeogift(){
        mGeogiftDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w(TAG, "geogiftFB retrieving");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GeogiftFB geogift = snapshot.getValue(GeogiftFB.class);
                    if(!geogift.isVisited() && !geogift.getUserCreatorUID().equals(userID)) {
                        mGeofenceList.add(new Geofence.Builder()
                                .setRequestId(geogift.getAddress()) //TODO change!!!
                                .setCircularRegion(Double.parseDouble(geogift.getLat()),
                                                   Double.parseDouble(geogift.getLon()),
                                                   GEOFENCE_RADIUS
                                )
                                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                                .build());
                    }
                }


                buildGoogleApiClient();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    private GeofencingRequest getGeofencingRequest() {
        Log.d(TAG, "createGeofencingRequest");
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }


    public void addGeofencesOnLoad() {
        if (!googleApiClient.isConnected()) {
            Log.w(TAG, "googleApiclient nont connected");
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            //logSecurityException(securityException);
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( mContext, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                mContext, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "googleApi connected");
        if(checkPermissionAccessFineLocation(mContext)) {
            addGeofencesOnLoad();
        }
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
