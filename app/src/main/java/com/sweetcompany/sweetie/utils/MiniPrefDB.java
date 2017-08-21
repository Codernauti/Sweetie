package com.sweetcompany.sweetie.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by ghiro on 19/08/2017.
 */

/*
 *  The "‚‗‚" character is not a comma, it is the SINGLE LOW-9 QUOTATION MARK unicode 201A
 *  and unicode 2017 that are used for separating the items in a list.
 */

public class MiniPrefDB {

    public static final String GEOGIFT_SET = "geogift_set";

    private SharedPreferences preferences;

    public MiniPrefDB(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public void addGeogiftToSet(String geogiftKey){
        Set<String> setId = preferences.getStringSet(GEOGIFT_SET, null);
        //The second argument 'null' means if there is no value for key "id", it will return null
        if(setId == null){
            setId = new HashSet<String>();
        }
        setId.add(geogiftKey);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(GEOGIFT_SET, setId);
        editor.commit();
    }

    public Set<String> getGeogiftSet(){
        Set<String> setId = preferences.getStringSet(GEOGIFT_SET, null);
        if(setId == null){
            setId = new HashSet<String>();
        }
        setId.add("empty");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(GEOGIFT_SET, setId);

        return  setId;
    }

}
