package com.sweetcompany.sweetie.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.UserMonitorService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by lucas on 22/05/2017.
 */

public class Utility {

    public static void removePreference(Context context, String key) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(key)
                .apply();
    }

    //How to use: pass the context, a key string and the data string; returns true if successfully saved
    public static void saveStringPreference(Context context, String key, String data) {
        Log.d("Save string preference", key + ": " + data);

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, data)
                .apply();
    }

    /**
     * Method for getting a string from shared preferences
     * How to use: pass the context and one of key strings above
     * if the result string doesn't exist it returns the string "error"
     * @param context
     * @param key
     * @return
     */
    public static String getStringPreference(Context context,String key){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, SharedPrefKeys.DEFAULT_VALUE);
    }

    public static void saveIntPreference(Context context, String key, int data) {
        Log.d("Save int preference", key + ": " + data);

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(key, data)
                .apply();
    }

    public static int getIntPreference(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(key, SharedPrefKeys.DEFAULT_INT_VALUE);
    }

    public static int getUserRelationshipStatus(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(key, UserMonitorService.SINGLE);
    }

    public static int getKeyboardHeightFromPreference(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        if (key.equals(SharedPrefKeys.KB_HEIGHT)) {
            int data = sp.getInt(key, (int)context.getResources().getDimension(R.dimen.keyboard_height_default));
            Log.d("Get height kb pref", key + ": " + data);
            return data;
        }
        else {
            int data = sp.getInt(key, SharedPrefKeys.DEFAULT_INT_VALUE);
            Log.d("Get height kb pref", key + ": " + data);
            return data;
        }
    }

    public static void saveBooleanPreference(Context context, String key, boolean value) {
        Log.d("Save boolean pref", key + ": " + value);

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static boolean getBooleanPreference(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(key, SharedPrefKeys.DEFAULT_BOOLEAN_VALUE);
    }


    public static Double getDoublePreference(Context context, String key){
        SharedPreferences setting = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        Double d = Double.parseDouble(setting.getString(key, SharedPrefKeys.DEFAULT_VALUE));
        return d;
    }

    public static boolean isImageAvaibleInLocal(String uriLocal){
        // TODO: this method remove too less complexity, redundancy remain into code base
        return (new File(uriLocal)).isFile();
    }

    public static void addGeofenceToSharedPreference(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        ArrayList<String> stringList = getGeofenceKeyList(context);
        stringList.add(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(SharedPrefKeys.GEOGIFT_SET, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public static ArrayList<String> getGeofenceKeyList(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(SharedPrefKeys.GEOGIFT_SET, ""), "‚‗‚")));
    }

    public static void clearSharedPreferences(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .clear()
                .apply();
    }

    public static void printLogAllSharedPreferences(Context context) {
        Map<String,?> keys = PreferenceManager.getDefaultSharedPreferences(context).getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("SharedPreference", entry.getKey() + ": " + entry.getValue().toString());
        }
    }
}
