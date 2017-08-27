package com.sweetcompany.sweetie;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.UserFB;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by Eduard on 15-Jul-17.
 */

public class UserMonitorService extends Service {

    private static final String TAG = "UserMonitorService";

    // monitor the user relationship status
    public static final int SINGLE = 0;
    public static final int COUPLED = 1;
    public static final int BREAK_SINGLE = 2;

    private DatabaseReference mUserNode;
    private ValueEventListener mUserListener;

    // Data we need to monitor (use a UserFB maybe?)
    private String mCoupleUid;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String userUid = Utility.getStringPreference(this, SharedPrefKeys.USER_UID);
        mCoupleUid = Utility.getStringPreference(this, SharedPrefKeys.COUPLE_UID);

        mUserNode = FirebaseDatabase.getInstance().getReference("users/" + userUid);

        mUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFB newUserData = dataSnapshot.getValue(UserFB.class);

                if (newUserData != null && newUserData.getCoupleInfo() != null) {

                    String newCoupleUidData = newUserData.getCoupleInfo().getActiveCouple();
                    // TODO: get partner username not UID
                    String partnerName = newUserData.getFuturePartner();
                    Utility.saveStringPreference(UserMonitorService.this,
                            SharedPrefKeys.PARTNER_USERNAME, partnerName);

                    if (newCoupleUidData != null) {
                        if (!newCoupleUidData.equals(mCoupleUid)) {
                            Log.d(TAG, "couple_uid is changed!");

                            Utility.saveStringPreference(UserMonitorService.this,
                                    SharedPrefKeys.COUPLE_UID, newCoupleUidData);
                            Utility.saveIntPreference(UserMonitorService.this,
                                    SharedPrefKeys.USER_RELATIONSHIP_STATUS, COUPLED);
                        }
                        // else coupleUid remains the same
                    }
                    else {
                        if (!mCoupleUid.equals(SharedPrefKeys.DEFAULT_VALUE)) {
                            Log.d(TAG, "couple break!");

                            Utility.saveStringPreference(UserMonitorService.this,
                                    SharedPrefKeys.COUPLE_UID, SharedPrefKeys.DEFAULT_VALUE);
                            Utility.saveIntPreference(UserMonitorService.this,
                                    SharedPrefKeys.USER_RELATIONSHIP_STATUS, BREAK_SINGLE);
                        }
                        // else mCouple == DEFAULT.VALUE -> the user is single
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        mUserNode.addValueEventListener(mUserListener);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUserNode.removeEventListener(mUserListener);
    }
}
