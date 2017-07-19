package com.sweetcompany.sweetie.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

    //Method for saving a shared preference
    //How to use: pass the context, a key string and the data string; returns true if successfully saved
    //
     static public boolean saveStringPreference(Context context, String key, String data) {
         Log.d("Save Preference", key + ": " + data);
         SharedPreferences settings = context.getSharedPreferences(key, 0);
         SharedPreferences.Editor editor = settings.edit();
         editor.putString(key,data);
         return editor.commit();
    }

    //
    static public boolean checkPreferencesSetted(Context context){
        if(getStringPreference(context, USER_UID) != DEFAULT_VALUE){
            if(getStringPreference(context,MAIL) != DEFAULT_VALUE){
                return true;
            }
        }
        return false;
    }

    //Method for getting a string from shared preferences
    //How to use: pass the context and one of key strings above
    //if the result string doesn't exist it returns the string "error"
    static public String getStringPreference(Context context,String key){
        SharedPreferences setting = context.getSharedPreferences(key, 0);
        return setting.getString(key, DEFAULT_VALUE);
    }

}
