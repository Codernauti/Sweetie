package com.sweetcompany.sweetie.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ghiro on 30/08/2017.
 */

public class FirebaseGeogiftIntentController {

    private static final String TAG = "GeogiftIntentController";

    private DatabaseReference mActionsDbReference;
    private DatabaseReference mGeogiftDbReference;

    String mCoupleUid;
    String mUserUid;

    public FirebaseGeogiftIntentController(String coupleUid, String userUid) {
        mActionsDbReference = FirebaseDatabase.getInstance().getReference(Constraints.ACTIONS + "/" + coupleUid);
        mGeogiftDbReference = FirebaseDatabase.getInstance().getReference(Constraints.GEOGIFTS + "/" + coupleUid);
        mCoupleUid = coupleUid;
        mUserUid = userUid;
    }

    public void setTriggeredGeogift(String actionKey, String geogiftKey, String dateTime) {

        mActionsDbReference.child(actionKey + "/" + "isTriggered").setValue(true);

        Map<String, Object> updates = new HashMap<>();
        updates.put(geogiftKey + "/" + "isTriggered", true);
        updates.put(geogiftKey + "/" + "datetimeVisited", dateTime);

        mGeogiftDbReference.updateChildren(updates);
    }

}
