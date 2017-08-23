package com.sweetcompany.sweetie.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.sweetcompany.sweetie.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lucas on 22/05/2017.
 */

public class Utility {

    // Key strings for get a shared preference
    public static final String USER_UID = "user_uid";
    public static final String MAIL = "mail";
    public static final String USERNAME = "username";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String GENDER = "gender";
    public static final String PARTNER_UID = "partner_uid";
    public static final String COUPLE_UID = "couple_uid";
    public static final String FUTURE_PARTNER_PAIRING_REQUEST = "futurePartner";
    public static final String DEFAULT_VALUE = "error";

    public static final String KB_HEIGHT = "keyboard_height";

    private static final int DEFAULT_INT_VALUE = 0;

    public static final String KEY_GEOFENCE_LAT = "GEOFENCE LATITUDE";
    public static final String KEY_GEOFENCE_LON = "GEOFENCE LONGITUDE";
    public static final String GEOGIFT_SET = "GEOGIFT_SET_KEYS";


    //Method for saving a shared preference
    //How to use: pass the context, a key string and the data string; returns true if successfully saved
    //
     static public boolean saveStringPreference(Context context, String key, String data) {
         Log.d("Save string preference", key + ": " + data);
         SharedPreferences settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = settings.edit();
         editor.putString(key,data);
         return editor.commit();
    }

    //
    static public boolean checkPreferencesSetted(Context context){
        // TODO: use equal() !!!
        if(getStringPreference(context, USER_UID) != DEFAULT_VALUE){
            if(getStringPreference(context,MAIL) != DEFAULT_VALUE){
                return true;
            }
        }
        return false;
    }

    /**
     * Method for getting a string from shared preferences
     * How to use: pass the context and one of key strings above
     * if the result string doesn't exist it returns the string "error"
     * @param context
     * @param key
     * @return
     */
    static public String getStringPreference(Context context,String key){
        SharedPreferences setting = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return setting.getString(key, DEFAULT_VALUE);
    }

    static public boolean saveIntPreference(Context context, String key, int data) {
        Log.d("Save int preference", key + ": " + data);
        SharedPreferences settings = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return settings.edit()
                        .putInt(key, data)
                        .commit();
    }

    static public int getIntPreference(Context context, String key) {
        SharedPreferences setting = context.getSharedPreferences(key, Context.MODE_PRIVATE);

        if (key.equals(KB_HEIGHT)) {
            int data = setting.getInt(key,
                    (int)context.getResources().getDimension(R.dimen.keyboard_height_default));
            Log.d("Get int preference", key + ": " + data);
            return data;
        }
        else {
            int data = setting.getInt(key, DEFAULT_INT_VALUE);
            Log.d("Get int preference", key + ": " + data);
            return data;
        }
    }

    static public Double getDoublePreference(Context context, String key){
        SharedPreferences setting = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        Double d = Double.parseDouble(setting.getString(key, DEFAULT_VALUE));
        return d;
    }

    public static boolean isImageAvaibleInLocal(String uriLocal){
        boolean isAvaible = false;
        if(new File(uriLocal).isFile()) isAvaible = true;
        return isAvaible;
    }

    public static void addGeofenceToSharedPreference(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        ArrayList<String> stringList = getGeofenceKeyList(context);
        stringList.add(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(GEOGIFT_SET, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public static ArrayList<String> getGeofenceKeyList(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(GEOGIFT_SET, ""), "‚‗‚")));
    }

}
