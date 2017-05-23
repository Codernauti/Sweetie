package com.sweetcompany.sweetie.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import java.util.Date;

/**
 * Created by lucas on 22/05/2017.
 */

public class Utility {
     static public boolean saveStringPreference(Context context, String name, String data){
        SharedPreferences settings = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name,data);
        return editor.commit();
    }
    static public String getStringPreference(Context context,String name){
        SharedPreferences setting = context.getSharedPreferences(name, 0);
        return setting.getString(name,"error");
    }

    // TODO
    // Give dateTime
    // return data formatted for Actions
    // es: Give (24/05/17 22:00:01)
    //     if date of today             return "22:00"
    //     if date of yesterday         return "TOMORROW/IERI"
    //     if date past                 return "24/05/17"
    public String getDateFormattedForActions(String data){
        String mData = data;

        return mData;
    }
}
