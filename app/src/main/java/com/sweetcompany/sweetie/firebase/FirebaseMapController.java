package com.sweetcompany.sweetie.firebase;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.GeogiftFB;

/**
 * Created by ghiro on 24/08/2017.
 */

public class FirebaseMapController {

    private static final String TAG = "FbMapController";

    private final DatabaseReference mDatabaseRef;
    private final DatabaseReference mGalleriesDbReference;
    private final DatabaseReference mGeogiftsDbReference;

    private String mCoupleUid = null;
    private String mUserUid = null;

    private ChildEventListener mGalleriesEventListener;
    private ChildEventListener mGeogiftsEventListener;

    private MapGeogiftControllerListener mGeogiftListener = null;
    private MapGalleryControllerListener mGalleryListener = null;

    public interface MapGeogiftControllerListener {
        void onGeogiftAdded(GeogiftFB geogift);
        void onGeogiftRemoved(GeogiftFB geogift);
    }

    public interface MapGalleryControllerListener {
        void onGalleryAdded(GalleryFB gallery);
        void onGalleryRemoved(GalleryFB gallery);
        void onGalleryChanged(GalleryFB gallery);
    }


    public FirebaseMapController(String coupleUid, String userUid) {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mGalleriesDbReference = mDatabaseRef.child(Constraints.GALLERIES + "/" + coupleUid);
        mGeogiftsDbReference = mDatabaseRef.child(Constraints.GEOGIFTS + "/" + coupleUid);

        mCoupleUid = coupleUid;
        mUserUid = userUid;
    }

    public void addGeogiftListener(MapGeogiftControllerListener listener) {
        mGeogiftListener = listener;
    }

    public void addGalleryListener(MapGalleryControllerListener listener){
        mGalleryListener = listener;
    }

    public void attachNetworkDatabase() {

        // Galleries

        if (mGalleriesEventListener == null) {
            mGalleriesEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    GalleryFB newGallery = dataSnapshot.getValue(GalleryFB.class);
                    newGallery.setKey(dataSnapshot.getKey());

                    if(newGallery.getLatitude() != null && newGallery.getLongitude() != null
                       && newGallery.getUriCover() != null) {
                        if (mGalleryListener != null) {
                            mGalleryListener.onGalleryAdded(newGallery);
                        }
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    GalleryFB newGallery = dataSnapshot.getValue(GalleryFB.class);
                    newGallery.setKey(dataSnapshot.getKey());

                    if (mGalleryListener != null) {
                        mGalleryListener.onGalleryChanged(newGallery);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    GalleryFB newGallery = dataSnapshot.getValue(GalleryFB.class);
                    newGallery.setKey(dataSnapshot.getKey());

                    if (mGalleryListener != null) {
                        mGalleryListener.onGalleryRemoved(newGallery);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            mGalleriesDbReference.addChildEventListener(mGalleriesEventListener);
        }

        // Geogifts

        if (mGeogiftsEventListener == null) {
            mGeogiftsEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    GeogiftFB newGeogift = dataSnapshot.getValue(GeogiftFB.class);
                    newGeogift.setKey(dataSnapshot.getKey());

                    if(newGeogift.getUserCreatorUID().equals(mUserUid) || newGeogift.getIsTriggered() == true) {
                        if (mGeogiftListener != null) {
                            mGeogiftListener.onGeogiftAdded(newGeogift);
                        }
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    //do nothing
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    GeogiftFB removedGeogift = dataSnapshot.getValue(GeogiftFB.class);
                    removedGeogift.setKey(dataSnapshot.getKey());
                    if (mGeogiftListener != null) {
                        mGeogiftListener.onGeogiftRemoved(removedGeogift);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            mGeogiftsDbReference.addChildEventListener(mGeogiftsEventListener);
        }
    }

    public void detachNetworkDatabase() {
        if (mGeogiftsEventListener != null) {
            mGeogiftsDbReference.removeEventListener(mGeogiftsEventListener);
        }
        mGeogiftsEventListener = null;

        if (mGalleriesEventListener != null) {
            mGalleriesDbReference.removeEventListener(mGalleriesEventListener);
        }
        mGalleriesEventListener = null;
    }

}