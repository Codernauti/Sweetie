package com.sweetcompany.sweetie.geogift;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;
import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.MainActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseGeogiftIntentController;
import com.sweetcompany.sweetie.utils.DataMaker;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 10/08/2017.
 */

public class GeofenceTrasitionService extends IntentService {

    private static final String TAG = "GeofenceTrasitionServ";

    public static final int GEOFENCE_NOTIFICATION_ID = 4040;
    public static final String GEOGIFT_ACTION_KEY = "ACTION_KEY";
    public static final String GEOGIFT_KEY = "GEOGIFT_KEY";

    private static final int NOTIFICATION_ID = 400;

    private FirebaseGeogiftIntentController mController = null;

    private NotificationManager mNotificationManager;

    public GeofenceTrasitionService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        mController = new FirebaseGeogiftIntentController(Utility.getStringPreference(this, SharedPrefKeys.COUPLE_UID), Utility.getStringPreference(this, SharedPrefKeys.USER_UID));

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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

        //TODO check if geogift is still alive

        // Check if the transition type is of interest
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ) {

            Log.d(TAG, "geofence triggered!");

            //Set geogift triggered
            String actionKey = intent.getStringExtra(GEOGIFT_ACTION_KEY);
            String geogiftKey = intent.getStringExtra(GEOGIFT_KEY);
            String dateTime = DataMaker.get_UTC_DateTime();

            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            String geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences );

            // Send notification details as a String
            //sendNotification( geofenceTransitionDetails );
            //TODO
            String msg = "Geogift finded!";
            sendNotification( msg, geogiftKey );

            mController.setTriggeredGeogift(actionKey, geogiftKey, dateTime);

        }
    }


    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            triggeringGeofencesList.add( geofence.getRequestId() );
        }

        String status = null;
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ) {
            status = "Entering ";
        }

        return status + TextUtils.join( ", ", triggeringGeofencesList);
    }

    private void sendNotification( String msg, String geogiftKey ) {
        Log.i(TAG, "sendNotification: " + geogiftKey );

        // init intent for open GeogiftDoneActivity
        Intent intent;
        PendingIntent pendingIntent;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        android.support.v7.app.NotificationCompat.Action replyAction;

        // start GeogiftDoneActivity
        intent = new Intent(this, GeogiftDoneActivity.class)
                .putExtra(GeogiftDoneActivity.GEOGIFT_DATABASE_KEY, geogiftKey)
                .putExtra(GeogiftDoneActivity.GEOGIFT_TITLE, "title fake");

        stackBuilder.addParentStack(DashboardActivity.class)    // start MainActivity
                .addParentStack(GeogiftDoneActivity.class)         // start DashboardActivity
                .addNextIntent(intent);

        pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT
                | PendingIntent.FLAG_ONE_SHOT);

        replyAction = new android.support.v7.app.NotificationCompat.Action(R.drawable.action_gift_icon, "Open", pendingIntent);

        // Creating and sending Notification
        Notification notification= new android.support.v7.app.NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_msg_notification)
                .setContentTitle(msg)
                //TODO //.setContentText(contentText)
                .setColor(ContextCompat.getColor(this, R.color.rosa_sweetie))
                .addAction(replyAction)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .build();

        mNotificationManager.notify(GEOFENCE_NOTIFICATION_ID, notification);
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

}
