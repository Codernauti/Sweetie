package com.sweetcompany.sweetie.firebase;

import android.support.annotation.CallSuper;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.ActionFB;

import java.util.Map;

/**
 * Created by Eduard on 05-Sep-17.
 */

public abstract class FirebaseGeneralActionController {

    private static final String SUPER_TAG = "GeneralActionController";

    private final String mUserUid;
    private final String mPartnerUid;

    private final String mActionUrl;    // actions/<couple_uid>/<action_uid>

    private final DatabaseReference mActionRef;
    private final DatabaseReference mUserNotificCounterRef;
    private ValueEventListener mActionListener;

    private int mPartnerCounter;                  // send notification to partner
    private String mPartnerNotificCounterUrl;     // acions/<couple_uid>/<action_uid>/notificationCounters/<partner_uid>


    FirebaseGeneralActionController(String coupleUid, String userUid, String partnerUid,
                                           String parentActionUid) {
        mUserUid = userUid;
        mPartnerUid = partnerUid;

        mActionUrl = Constraints.ACTIONS + "/" + coupleUid + "/" + parentActionUid;
        String mUserNotificationCounterUrl = mActionUrl + "/"
                + Constraints.Actions.NOTIFICATION_COUNTER + "/"
                + userUid + "/"
                + Constraints.Actions.COUNTER;

        mPartnerNotificCounterUrl = mActionUrl + "/"
                + Constraints.Actions.NOTIFICATION_COUNTER + "/"
                + partnerUid + "/"
                + Constraints.Actions.COUNTER;

        mActionRef = FirebaseDatabase.getInstance().getReference(mActionUrl);
        mUserNotificCounterRef = FirebaseDatabase.getInstance().getReference(mUserNotificationCounterUrl);
    }

    @CallSuper
    public void attachListeners() {
        if (mActionListener == null) {
            mActionListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ActionFB action = dataSnapshot.getValue(ActionFB.class);

                    // TODO: exclude error partner
                    if (action != null && action.getNotificationCounters() != null) {

                        // sync notification partner counter from remote database
                        if (action.getNotificationCounters().containsKey(mPartnerUid)) {
                            Log.d(SUPER_TAG, "getCounter of partner");
                            mPartnerCounter = action.getNotificationCounters().get(mPartnerUid).getCounter();
                        }

                        // consume notification from partner
                        if (action.getNotificationCounters().containsKey(mUserUid)) {
                            Log.d(SUPER_TAG, "reset counter of user");
                            mUserNotificCounterRef.setValue(0);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            mActionRef.addValueEventListener(mActionListener);
        }
    }

    @CallSuper
    public void detachListeners() {
        if (mActionListener != null) {
            mActionRef.removeEventListener(mActionListener);
        }
        mActionListener = null;
    }

    /**
     * Helper method that increment the partner's notification counter of opened action
     * Call it when you update the action fields (such as "Description")
     * @param updates
     */
    void updateNotificationCounter(Map<String, Object> updates) {
        Log.d(SUPER_TAG, "++partnerCounter");
        updates.put(mPartnerNotificCounterUrl, ++mPartnerCounter);
    }

}
