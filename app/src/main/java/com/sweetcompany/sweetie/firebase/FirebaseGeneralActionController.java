package com.sweetcompany.sweetie.firebase;

import android.support.annotation.CallSuper;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.ActionNotification;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eduard on 05-Sep-17.
 */

abstract class FirebaseGeneralActionController {

    private static final String SUPER_TAG = "GeneralActionController";

    private final String mUserUid;
    private final String mPartnerUid;

    private final String mPartnerNotificationUrl;     // actions/<couple_uid>/<action_uid>/notificationCounters/<partner_uid>
    // private final String mNotificationCountersUrl;  //actions/<couple_uid>/<action_uid>notificationCounters

    private final DatabaseReference mNotificationCountersRef;
    private ValueEventListener mNotificationCountersListener;

    // caching for know when send a notification to partner
    private int mPartnerCounter;
    private Map<String, Boolean> mPartnerUpdatedElements;


    FirebaseGeneralActionController(String coupleUid, String userUid, String partnerUid,
                                           String parentActionUid) {
        mUserUid = userUid;
        mPartnerUid = partnerUid;

        String mActionUrl = Constraints.ACTIONS + "/" + coupleUid + "/" + parentActionUid;

        String mNotificationCountersUrl = mActionUrl + "/"
                + Constraints.Actions.NOTIFICATION_COUNTERS;

        mPartnerNotificationUrl = mActionUrl + "/"
                + Constraints.Actions.NOTIFICATION_COUNTERS + "/"
                + partnerUid;

        mNotificationCountersRef = FirebaseDatabase.getInstance().getReference(mNotificationCountersUrl);
    }

    @CallSuper
    public void attachListeners() {
        /*if (mActionListener == null) {
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
            mActionRef.addValueEventListener(mActionListener);*/
        if (mNotificationCountersListener == null) {
            mNotificationCountersListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<Map<String, ActionNotification>> typeIndicator =
                            new GenericTypeIndicator<Map<String, ActionNotification>>() {};
                    Map<String, ActionNotification> notificationCounters = dataSnapshot.getValue(typeIndicator);

                    // TODO: exclude error partner
                    if (notificationCounters != null) {

                        // sync notification partner counter from remote database
                        if (notificationCounters.containsKey(mPartnerUid)) {
                            Log.d(SUPER_TAG, "getCounter of partner");
                            mPartnerCounter = notificationCounters.get(mPartnerUid).getCounter();
                            mPartnerUpdatedElements = notificationCounters.get(mPartnerUid).getUpdatedElements();
                        }

                        // consume notification from partner
                        if (notificationCounters.containsKey(mUserUid)) {
                            Log.d(SUPER_TAG, "reset notification of user");
                            HashMap<String, Object> updates = new HashMap<>();
                            updates.put(mUserUid + "/" + Constraints.Actions.COUNTER, 0);
                            updates.put(mUserUid + "/" + Constraints.Actions.UPDATED_ELEMENTS, null);
                            mNotificationCountersRef.updateChildren(updates);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mNotificationCountersRef.addValueEventListener(mNotificationCountersListener);
        }
    }

    @CallSuper
    public void detachListeners() {
        if (mNotificationCountersListener != null) {
            mNotificationCountersRef.removeEventListener(mNotificationCountersListener);
        }
        mNotificationCountersListener = null;
    }

    /**
     * Helper method that increment the partner's notification counter of opened action
     * and insert the updated element into a list used to decrease the counter in case of deletion
     * Call it when you update the action fields (such as "Description")
     * @param updates
     */
    void updateNotificationCounterAfterInsertion(Map<String, Object> updates, String elementUid) {
        Log.d(SUPER_TAG, "++partnerCounter and push newElement");

        updates.put(mPartnerNotificationUrl + "/"
                    + Constraints.Actions.COUNTER,
                    ++mPartnerCounter);

        updates.put(mPartnerNotificationUrl + "/"
                    + Constraints.Actions.UPDATED_ELEMENTS + "/"
                    + elementUid,
                    true);
    }

    void updateNotificationCounterAfterDeletion(Map<String, Object> updates, String elementUid) {
        if (mPartnerUpdatedElements != null &&
            mPartnerUpdatedElements.containsKey(elementUid)) {

            Log.d(SUPER_TAG, "element contained, decrease counter");

            // decrease counter
            updates.put(mPartnerNotificationUrl + "/"
                        + Constraints.Actions.COUNTER,
                        --mPartnerCounter);

            updates.put(mPartnerNotificationUrl + "/"
                        + Constraints.Actions.UPDATED_ELEMENTS + "/"
                        + elementUid,
                        null);
        }
    }

}
