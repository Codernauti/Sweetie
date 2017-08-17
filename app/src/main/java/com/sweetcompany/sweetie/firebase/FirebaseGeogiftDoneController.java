package com.sweetcompany.sweetie.firebase;


import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.geogift.GeoItem;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.GeogiftFB;
import com.sweetcompany.sweetie.model.MediaFB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 17/08/2017.
 */

public class FirebaseGeogiftDoneController {

    private static final String TAG = "FbGeogiftDoneController";

    private final DatabaseReference mActionsDbReference;
    private final DatabaseReference mGeogiftDbReference;

    private final String coupleID;

    private ValueEventListener mGeogiftListener;

    private List<FirebaseGeogiftDoneController.GeogiftDoneControllerListener> mListeners = new ArrayList<>();

    public interface GeogiftDoneControllerListener {
        void onGeogiftChanged(GeogiftFB geogift);
    }


    public FirebaseGeogiftDoneController(String coupleUid, String geogiftKey, String actionKey) {
        mGeogiftDbReference = FirebaseDatabase.getInstance()
                .getReference(Constraints.GEOGIFT + "/" + coupleUid + "/" + geogiftKey);
        mActionsDbReference = FirebaseDatabase.getInstance()
                .getReference(Constraints.ACTIONS + "/" + coupleUid + "/" + actionKey);

        coupleID = coupleUid;
    }

    public void addListener(FirebaseGeogiftDoneController.GeogiftDoneControllerListener listener) {
        mListeners.add(listener);
    }

    public void attachListeners() {
        if (mGeogiftListener == null) {
            mGeogiftListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GeogiftFB geogift = dataSnapshot.getValue(GeogiftFB.class);
                    geogift.setKey(dataSnapshot.getKey());

                    for (FirebaseGeogiftDoneController.GeogiftDoneControllerListener listener : mListeners) {
                        listener.onGeogiftChanged(geogift);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mGeogiftDbReference.addValueEventListener(mGeogiftListener);
        }
    }

    public void detachListeners() {
        if (mGeogiftListener != null) {
            mGeogiftDbReference.removeEventListener(mGeogiftListener);
        }
        mGeogiftListener = null;
    }

}