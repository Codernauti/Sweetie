package com.sweetcompany.sweetie.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ghiro on 05/06/2017.
 */

public class DataMaker {

    public static SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static String get_UTC_DateTime() {
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar today = Calendar.getInstance();

        return isoFormat.format(today.getTime());
    }

    public static String get_Local_DateTime(){
        isoFormat.setTimeZone(TimeZone.getDefault());
        Calendar today = Calendar.getInstance();

        return isoFormat.format(today.getTime());
    }

    public static Date UTC_to_Local (String utc){
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsed = null;
        try {
            parsed = isoFormat.parse(utc);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsed;
    }

    public static String getHH_ss_Local (String utc){
        Date local = UTC_to_Local(utc);

        DateFormat localFormat = new SimpleDateFormat("HH:mm");
        localFormat.setTimeZone(TimeZone.getDefault());
        return localFormat.format(local);
    }

    public static String get_dd_MM_Local (String utc){
        Date local = UTC_to_Local(utc);

        DateFormat localFormat = new SimpleDateFormat("dd/MM");
        localFormat.setTimeZone(TimeZone.getDefault());
        return localFormat.format(local);
        //return utc;
    }

    public static String get_dd_MM_yy_Local (String utc){
        Date local = UTC_to_Local(utc);

        DateFormat localFormat = new SimpleDateFormat("dd/MM/yyyy");
        localFormat.setTimeZone(TimeZone.getDefault());
        return localFormat.format(local);
        //return utc;
    }


    // Give dateTime
    // return data formatted for Actions
    // es: Give (yyyy-MM-dd'T'HH:mm:ss)
    //     if date of today             return "HH:mm"
    //     if date of yesterday         return "YDA/IERI"
    //     if date past                 return "dd/MM/yyyy"
    //     if Local en_US               return "MM/dd/yy"
    //     if Local Asiatic        return "yy/MM/DD"            TODO
    //     else (European and other)    return "dd/MM/yy"
    public static String get_Date_4_Action(String utc) throws ParseException {
        Date local = UTC_to_Local(utc);

        String out = "";
        DateFormat localFormatDate = null;
        DateFormat localFormatTime = null;


        if(isToday(utc)){
            localFormatTime = new SimpleDateFormat("HH:mm");
            localFormatTime.setTimeZone(TimeZone.getDefault());
            out = localFormatTime.format(local);
        }
        else{
            String current = Locale.getDefault().toString();

            if(current.equals("en_US"))
            {
                localFormatDate = new SimpleDateFormat("MM/dd/yy");
                localFormatDate.setTimeZone(TimeZone.getDefault());
            }else
            {
                localFormatDate = new SimpleDateFormat("dd/MM/yy");
                localFormatDate.setTimeZone(TimeZone.getDefault());
            }
            out = localFormatDate.format(local);
        }

        return out;
    }

    public static boolean isToday(String utc) throws ParseException {
        boolean b = false;

        if(get_dd_MM_Local(get_UTC_DateTime()).equals(get_dd_MM_Local(utc))){
            b = true;
        }
        return b;
    }

    public static String getIsoFormatDateFromDate(@NonNull Date date) {
        return isoFormat.format(date);
    }

    public static Date getDateFromIsoFormatString(@NonNull String dateString) {
        Date messageDate = new Date();
        if (dateString != null) {
            try {
                messageDate = isoFormat.parse(dateString);
            } catch (ParseException ex) {
                Log.w("utils.DataMaker", ex.getMessage());
            }
        }
        return messageDate;
    }

}
