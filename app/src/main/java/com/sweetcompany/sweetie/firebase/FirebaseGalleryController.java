package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.PhotoFB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ghiro on 22/07/2017.
 */

public class FirebaseGalleryController {

    private static final String TAG = "FbGalleryController";

    private final DatabaseReference mGallery;
    private final DatabaseReference mGalleryPhotos;
    private final DatabaseReference mAction;

    private ValueEventListener mGalleryListener;
    private ChildEventListener mGalleryPhotosListener;


    private List<GalleryControllerListener> mListeners = new ArrayList<>();

    public interface GalleryControllerListener {
        void onGalleryChanged(GalleryFB gallery);

        void onPhotoAdded(PhotoFB message);
        void onPhotoRemoved(PhotoFB message);
        void onPhotoChanged(PhotoFB message);
    }


    public FirebaseGalleryController(String coupleUid, String galleryKey, String actionKey) {
        mGallery = FirebaseDatabase.getInstance()
                .getReference(Constraints.GALLERIES + "/" + coupleUid + "/" + galleryKey);
        mGalleryPhotos = FirebaseDatabase.getInstance()
                .getReference(Constraints.GALLERY_PHOTOS + "/" + coupleUid + "/" + galleryKey);
        mAction = FirebaseDatabase.getInstance()
                .getReference(Constraints.ACTIONS + "/" + coupleUid + "/" + actionKey);
    }

    public void addListener(GalleryControllerListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(GalleryControllerListener listener) {
        mListeners.remove(listener);
    }


    public void attachListeners() {
        if (mGalleryPhotosListener == null) {
            mGalleryPhotosListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    PhotoFB newPhoto = dataSnapshot.getValue(PhotoFB.class);
                    newPhoto.setKey(dataSnapshot.getKey());
                    //Log.d(TAG, "onPhotoAdded to gallery: " + newPhoto.getText() ???);

                    for (GalleryControllerListener listener : mListeners) {
                        listener.onPhotoAdded(newPhoto);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    PhotoFB newPhoto = dataSnapshot.getValue(PhotoFB.class);
                    newPhoto.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onChildChanged of gallery: " + newPhoto.getText());

                    for (GalleryControllerListener listener : mListeners) {
                        listener.onPhotoChanged(newPhoto);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    PhotoFB removedPhoto = dataSnapshot.getValue(PhotoFB.class);
                    //Log.d(TAG, "onPhotoRemoved from gallery: " + removedPhoto.getText());

                    for (GalleryControllerListener listener : mListeners) {
                        listener.onPhotoRemoved(removedPhoto);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            mGalleryPhotos.addChildEventListener(mGalleryPhotosListener);
        }

        if (mGalleryListener == null) {
            mGalleryListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // TODO: test
                    GalleryFB photo = dataSnapshot.getValue(GalleryFB.class);
                    photo.setKey(dataSnapshot.getKey());

                    for (GalleryControllerListener listener : mListeners) {
                        listener.onGalleryChanged(photo);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mGallery.addValueEventListener(mGalleryListener);
        }
    }

    public void detachListeners() {
        if (mGalleryListener != null) {
            mGallery.removeEventListener(mGalleryListener);
        }
        mGalleryListener = null;

        // TODO ??
        /*if (mGalleryPhotosListener != null) {
            mGalleryPhotosListener.removeEventListener(mGalleryPhotosListener);
        }
        mGalleryPhotosListener = null;*/
    }


    public void updatePhoto(PhotoFB photo) {
        Log.d(TAG, "Update PhotoFB: " + photo);

        DatabaseReference ref = mGalleryPhotos.child(photo.getKey()).child("bookmarked");
        ref.setValue(photo.isBookmarked());
    }

    // push message to db and update action of this gallery
    public void sendPhoto(PhotoFB photo) {
        Log.d(TAG, "Send PhotoFB: " + photo);

        // push a message into mGalleryPhotos reference
        mGalleryPhotos.push().setValue(photo);

        // update description and dataTime of action of this associated Gallery
        Map<String, Object> actionUpdates = new HashMap<>();
        //actionUpdates.put("description", photo.getText());
        actionUpdates.put("dataTime", photo.getDateTime());
        mAction.updateChildren(actionUpdates);
    }

    /*private void updateActionLastMessage(String actionKey, MessageFB msg){
        DatabaseReference mActionReference = FirebaseDatabase.getInstance()
                .getReference().child("actions").child(actionKey).child("description");
        mActionReference.setValue(msg.getText());
        mActionReference = FirebaseDatabase.getInstance()
                .getReference().child("actions").child(actionKey).child("dataTime");
        mActionReference.setValue(msg.getDateTime());
    }*/


}