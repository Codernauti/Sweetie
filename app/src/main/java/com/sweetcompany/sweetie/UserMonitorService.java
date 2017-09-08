package com.sweetcompany.sweetie;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebaseUserController;
import com.sweetcompany.sweetie.model.CoupleInfoFB;
import com.sweetcompany.sweetie.model.UserFB;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by Eduard on 15-Jul-17.
 */

public class UserMonitorService extends Service implements FirebaseUserController.UserControllerListener {

    private static final String TAG = "UserMonitorService";

    // monitor the user relationship status
    public static final int SINGLE = 0;
    public static final int COUPLED = 1;
    public static final int BREAK_SINGLE = 2;

    private FirebaseUserController mController;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String userUid = Utility.getStringPreference(this, SharedPrefKeys.USER_UID);
        mController = new FirebaseUserController(userUid, this);
        mController.attachListener();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.detachListener();
    }


    // controller callback

    @Override
    public void onUserChange(@Nullable UserFB newUserData) {

        if (newUserData == null) {
            // Try to take out the user
            Utility.clearSharedPreferences(this);
            return;
        }

        // save user data
        Utility.saveStringPreference(UserMonitorService.this, SharedPrefKeys.USER_IMAGE_URI, newUserData.getImageUrl());

        // manage couple status
        String oldCoupleUid = Utility.getStringPreference(UserMonitorService.this, SharedPrefKeys.COUPLE_UID);

        if (newUserData.getCoupleInfo() != null) {
            CoupleInfoFB newCoupleInfo = newUserData.getCoupleInfo();
            String newCoupleUid = newCoupleInfo.getActiveCouple();

            if (newCoupleUid != null) {
                if (!newCoupleUid.equals(oldCoupleUid)) {
                    Log.d(TAG, "couple_uid is changed!");

                    Utility.saveStringPreference(UserMonitorService.this,
                            SharedPrefKeys.PARTNER_USERNAME, newCoupleInfo.getPartnerUsername());

                    Utility.saveStringPreference(UserMonitorService.this,
                            SharedPrefKeys.PARTNER_IMAGE_URI, newCoupleInfo.getPartnerImageUri());

                    Utility.saveStringPreference(UserMonitorService.this,
                            SharedPrefKeys.PARTNER_UID, newUserData.getFuturePartner());

                    Utility.saveBooleanPreference(UserMonitorService.this,
                            SharedPrefKeys.USER_RELATIONSHIP_STATUS_CHANGED, true);

                    Utility.saveStringPreference(UserMonitorService.this,
                            SharedPrefKeys.COUPLE_UID, newCoupleUid);
                    Utility.saveIntPreference(UserMonitorService.this,
                            SharedPrefKeys.USER_RELATIONSHIP_STATUS, COUPLED);
                }
                // else coupleUid remains the same
            }
            else {
                if (!oldCoupleUid.equals(SharedPrefKeys.DEFAULT_VALUE)) {
                    Log.d(TAG, "couple break!");

                    Utility.saveBooleanPreference(UserMonitorService.this,
                            SharedPrefKeys.USER_RELATIONSHIP_STATUS_CHANGED, true);

                    Utility.saveStringPreference(UserMonitorService.this,
                            SharedPrefKeys.COUPLE_UID, SharedPrefKeys.DEFAULT_VALUE);
                    Utility.saveIntPreference(UserMonitorService.this,
                            SharedPrefKeys.USER_RELATIONSHIP_STATUS, BREAK_SINGLE);
                }
                // else mCouple == DEFAULT.VALUE -> the user is single
            }
        }
    }
}
