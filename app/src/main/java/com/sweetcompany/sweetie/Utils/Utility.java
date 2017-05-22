package com.sweetcompany.sweetie.Utils;

import android.content.Context;
import android.content.SharedPreferences;

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
}
