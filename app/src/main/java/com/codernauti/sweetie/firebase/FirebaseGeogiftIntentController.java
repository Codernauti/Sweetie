package com.codernauti.sweetie.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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

    public void setTriggeredGeogift(String geogiftKey, String dateTime) {
        mActionsDbReference.child(geogiftKey + "/" + "isTriggered").setValue(true);

        Map<String, Object> updates = new HashMap<>();
        updates.put(geogiftKey + "/" + Constraints.Geogifts.IS_TRIGGERED, true);
        updates.put(geogiftKey + "/" + Constraints.Geogifts.DATE_TIME_VISITED, dateTime);

        mGeogiftDbReference.updateChildren(updates);
    }

    public boolean isGeogiftAvaible(){
        mGeogiftDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User Exists
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return false;
    }

}
