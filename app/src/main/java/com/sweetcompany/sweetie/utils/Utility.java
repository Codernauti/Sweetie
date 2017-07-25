package com.sweetcompany.sweetie.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sweetcompany.sweetie.R;

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
            int data = setting.getInt(key, R.dimen.keyboard_height_default);
            Log.d("Get int preference", key + ": " + data);
            return data;
        }
        else {
            int data = setting.getInt(key, DEFAULT_INT_VALUE);
            Log.d("Get int preference", key + ": " + data);
            return data;
        }
    }

}
