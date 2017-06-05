package com.sweetcompany.sweetie.Utils;


import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by ghiro on 05/06/2017.
 */

public class DataMaker {



    public static String get_UTC_DateTime() throws ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar today = Calendar.getInstance();

        return isoFormat.format(today.getTime());
    }

    public static String get_Local_DateTime() throws ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getDefault());
        Calendar today = Calendar.getInstance();

        return isoFormat.format(today.getTime());
    }

    public static String UTC_to_Local (String utc){
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getDefault());

        return isoFormat.format(utc);
    }

    public static String getHH_ss_Local (String utc){
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsed = null;
        try {
            parsed = isoFormat.parse(utc);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat localFormat = new SimpleDateFormat("HH:mm");
        localFormat.setTimeZone(TimeZone.getDefault());
        return localFormat.format(parsed);
    }

    public static String get_dd_MM_Local (String utc){
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsed = null;
        try {
            parsed = isoFormat.parse(utc);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat localFormat = new SimpleDateFormat("dd/MM");
        localFormat.setTimeZone(TimeZone.getDefault());
        return localFormat.format(parsed);
        //return utc;
    }


    // TODO
    // Give dateTime
    // return data formatted for Actions
    // es: Give (24/05/17 22:00:01)
    //     if date of today             return "22:00"
    //     if date of yesterday         return "TOMORROW/IERI"
    //     if date past                 return "24/05/17"
}
