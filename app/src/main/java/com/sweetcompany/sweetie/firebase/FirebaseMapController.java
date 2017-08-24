package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.model.GalleryFB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 24/08/2017.
 */

public class FirebaseMapController {

    private static final String TAG = "FbMapController";

    private final DatabaseReference mGalleriesDbReference;
    private final StorageReference mStorageRef;
    private final FirebaseStorage mStorage;

    private final String coupleID;

    private ValueEventListener mGalleryListener;
    private MapControllerListener mListener;

    public interface MapControllerListener {
        void updateGalleryList(List<GalleryFB> actions);
    }


    public FirebaseMapController(String coupleUid) {
        mGalleriesDbReference = FirebaseDatabase.getInstance()
                .getReference(Constraints.GALLERIES + "/" + coupleUid);
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        coupleID = coupleUid;
    }

    public void addListener(FirebaseMapController.MapControllerListener listener) {
        mListener = listener;
    }

    public void attachNetworkDatabase() {
        if (mGalleryListener == null) {
            mGalleryListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot actionsSnapshot) {
                    List<GalleryFB> galleries = new ArrayList<>();

                    for (DataSnapshot actionSnapshot : actionsSnapshot.getChildren()) {
                        GalleryFB action = actionSnapshot.getValue(GalleryFB.class);
                        galleries.add(action);
                    }

                    if (mListener != null) {
                        mListener.updateGalleryList(galleries);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }
            };

            // TODO test much more sorting
            mGalleriesDbReference.addValueEventListener(mGalleryListener);
        }
    }

    public void detachNetworkDatabase() {
        if (mGalleryListener != null) {
            mGalleriesDbReference.removeEventListener(mGalleryListener);
        }
        mGalleryListener = null;
    }

}