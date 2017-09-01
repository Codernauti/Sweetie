package com.sweetcompany.sweetie.utils;

/**
 * Created by Eduard on 26-Aug-17.
 */

// Key strings for get a shared preference
public interface SharedPrefKeys {

    String USER_UID = "user_uid";
    String MAIL = "mail";
    String USERNAME = "username";
    String PHONE_NUMBER = "phone_number";
    String USER_IMAGE_URI = "user_image_uri";
    String GENDER = "gender";

    String COUPLE_UID = "couple_uid";

    String PARTNER_UID = "partner_uid";
    String FUTURE_PARTNER_PAIRING_REQUEST = "future_partner";
    String PARTNER_USERNAME = "partner_username";
    String PARTNER_IMAGE_URI = "partner_image_uri";

    String USER_RELATIONSHIP_STATUS = "user_relationship_status";
    String USER_RELATIONSHIP_STATUS_CHANGED = "user_relationship_status_changed";

    // Default values
    String DEFAULT_VALUE = "error";
    int DEFAULT_INT_VALUE = 0;
    boolean DEFAULT_BOOLEAN_VALUE = false;

    // Utility values
    String KB_HEIGHT = "keyboard_height";
    String CHAT_FOREGROUND_UID = "chat_foreground_uid";

    String KEY_GEOFENCE_LAT = "GEOFENCE LATITUDE";
    String KEY_GEOFENCE_LON = "GEOFENCE LONGITUDE";
    String GEOGIFT_SET = "GEOGIFT_SET_KEYS";

    // Options sharedPreferences
    interface Options {
        String GEOGIFT_ENABLED = "geogift_enabled";
    }
}
