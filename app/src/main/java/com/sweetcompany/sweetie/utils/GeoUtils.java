package com.sweetcompany.sweetie.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by ghiro on 12/08/2017.
 */


public class GeoUtils {

    private static final String TAG = "GeoUtils";
    public static final int REQ_PERMISSION_UPDATE = 4001;

    // Check for permission to access Location
    static public boolean checkPermissionAccessFineLocation(Context context) {
        Log.d(TAG, "checkPermissionAccesFineLocation()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

}
