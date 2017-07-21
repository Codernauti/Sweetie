package com.sweetcompany.sweetie;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.couple.CoupleDetailsActivity;
import com.sweetcompany.sweetie.model.UserFB;
import com.sweetcompany.sweetie.utils.Utility;
import com.sweetcompany.sweetie.couple.CoupleActivity;

/**
 * Created by Eduard on 15-Jul-17.
 */

public class UserMonitorService extends Service {

    private static final String TAG = "UserMonitorService";

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
        String userUid = Utility.getStringPreference(this, Utility.USER_UID);
        mCoupleUid = Utility.getStringPreference(this, Utility.COUPLE_UID);

        mUserNode = FirebaseDatabase.getInstance().getReference("users/" + userUid);

        mUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFB newUserData = dataSnapshot.getValue(UserFB.class);

                if (newUserData.getCoupleInfo() != null) {
                    String newCoupleUidData = newUserData.getCoupleInfo().getActiveCouple();
                    // TODO: get partner username not UID
                    String partnerName = newUserData.getFuturePartner();

                    if (newCoupleUidData != null) {
                        if (!newCoupleUidData.equals(mCoupleUid)) {
                            Log.d(TAG, "couple_uid is changed!");
                            startCoupleActivity(newCoupleUidData, "You are now coupled with " + partnerName);
                        }
                        // else coupleUid remains the same
                    }
                    else {
                        if (!mCoupleUid.equals(Utility.DEFAULT_VALUE)) {
                            Log.d(TAG, "couple break!");
                            startCoupleActivity(Utility.DEFAULT_VALUE, "You break up with " + partnerName);
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

    private void startCoupleActivity(String newCoupleUid, String messageToShow) {
        Utility.saveStringPreference(this, Utility.COUPLE_UID, newCoupleUid);

        // start activity and clean the Task (back stack of activity)
        Intent intent = new Intent(this, CoupleActivity.class);
        intent.putExtra(CoupleActivity.MESSAGE_TO_SHOW_KEY, messageToShow);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
