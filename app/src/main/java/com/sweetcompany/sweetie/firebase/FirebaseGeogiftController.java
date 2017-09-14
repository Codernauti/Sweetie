package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.GeogiftFB;

public class FirebaseGeogiftController {

    private static final String TAG = "GeogiftController";

    private final DatabaseReference mGeogiftsDbReference;
    private ChildEventListener mGeogiftsListener;

    private GeogiftControllerListener mListener;
    private String userUid;

    public interface GeogiftControllerListener {
        void onAddedGeogift(GeogiftFB geogiftFB);
        void onRemovedGeogift(String geogiftKey);
    }


    public FirebaseGeogiftController(String coupleUid, String userUid) {
        mGeogiftsDbReference = FirebaseDatabase.getInstance().getReference(Constraints.GEOGIFTS + "/" + coupleUid);
        this.userUid = userUid;
    }

    public void setListener(GeogiftControllerListener listener){
        mListener = listener;
    }

    public void retrieveGeogiftFB(String geoKey) {
        mGeogiftsDbReference.child(geoKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w(TAG, "geogiftFB retrieving");

                GeogiftFB geogiftFB = dataSnapshot.getValue(GeogiftFB.class);

                /*for (GeogiftControllerListener listener : mListener) {
                    listener.onAddedGeogift(geogiftFB);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public void attachListenerDatabase() {
        if (mGeogiftsListener == null) {
            mGeogiftsListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    GeogiftFB geogiftFB = dataSnapshot.getValue(GeogiftFB.class);
                    geogiftFB.setKey(dataSnapshot.getKey());

                    if (!geogiftFB.getUserCreatorUID().equals(userUid) && !geogiftFB.getIsTriggered()) {
                        Log.d(TAG, "onGeogiftAdded");

                        if (mListener != null) {
                            mListener.onAddedGeogift(geogiftFB);
                        }
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    GeogiftFB geogiftFB = dataSnapshot.getValue(GeogiftFB.class);

                    if (mListener != null) {
                        mListener.onRemovedGeogift(geogiftFB.getKey());
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            };
            mGeogiftsDbReference.addChildEventListener(mGeogiftsListener);
        }
    }

    public void detachListenerDatabase(){
        if (mGeogiftsListener!= null){
            mGeogiftsDbReference.removeEventListener(mGeogiftsListener);
        }
        mGeogiftsListener = null;
    }
}
