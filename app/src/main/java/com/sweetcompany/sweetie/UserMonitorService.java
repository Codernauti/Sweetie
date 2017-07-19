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
import com.sweetcompany.sweetie.Utils.Utility;
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
                    String newCoupleUidData = newUserData.getCoupleInfo().getActiveCoupleUid();

                    if (!newCoupleUidData.equals(mCoupleUid)) {
                        Log.d(TAG, "couple_uid is changed!");
                        startCoupleActivity(newCoupleUidData);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        mUserNode.addValueEventListener(mUserListener);

        return super.onStartCommand(intent, flags, startId);
    }

    private void startCoupleActivity(String newCoupleUid) {
        Utility.saveStringPreference(this, Utility.COUPLE_UID, newCoupleUid);

        // TODO save into intent extra the partner user name?

        // start activity and clean the Task (back stack of activity)
        Intent intent = new Intent(this, CoupleActivity.class);
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
