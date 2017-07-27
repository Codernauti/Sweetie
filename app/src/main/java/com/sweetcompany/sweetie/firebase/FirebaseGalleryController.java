package com.sweetcompany.sweetie.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.MediaFB;

import java.io.File;
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
    private final StorageReference mStorageRef;
    private final FirebaseStorage mStorage;
    private final StorageReference imagesRef;


    private ValueEventListener mGalleryListener;
    private ChildEventListener mGalleryPhotosListener;


    private List<GalleryControllerListener> mListeners = new ArrayList<>();

    public interface GalleryControllerListener {
        void onGalleryChanged(GalleryFB gallery);

        void onMediaAdded(MediaFB message);
        void onMediaRemoved(MediaFB message);
        void onMediaChanged(MediaFB message);
    }


    public FirebaseGalleryController(String coupleUid, String galleryKey, String actionKey) {
        mGallery = FirebaseDatabase.getInstance()
                .getReference(Constraints.GALLERIES + "/" + coupleUid + "/" + galleryKey);
        mGalleryPhotos = FirebaseDatabase.getInstance()
                .getReference(Constraints.GALLERY_PHOTOS + "/" + coupleUid + "/" + galleryKey);
        mAction = FirebaseDatabase.getInstance()
                .getReference(Constraints.ACTIONS + "/" + coupleUid + "/" + actionKey);
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        imagesRef = mStorageRef.child("images");

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
                    MediaFB newMedia = dataSnapshot.getValue(MediaFB.class);
                    newMedia.setKey(dataSnapshot.getKey());
                    //Log.d(TAG, "onPhotoAdded to gallery: " + newPhoto.getText() ???);

                    for (GalleryControllerListener listener : mListeners) {
                        listener.onMediaAdded(newMedia);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    MediaFB newMedia = dataSnapshot.getValue(MediaFB.class);
                    newMedia.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onChildChanged of gallery: " + newMedia.getText());

                    for (GalleryControllerListener listener : mListeners) {
                        listener.onMediaChanged(newMedia);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    MediaFB removedMedia = dataSnapshot.getValue(MediaFB.class);
                    //Log.d(TAG, "onPhotoRemoved from gallery: " + removedPhoto.getText());

                    for (GalleryControllerListener listener : mListeners) {
                        listener.onMediaRemoved(removedMedia);
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


    public void updateMedia(MediaFB photo) {
        Log.d(TAG, "Update MediaFB: " + photo);

        DatabaseReference ref = mGalleryPhotos.child(photo.getKey()).child("bookmarked");
        ref.setValue(photo.isBookmarked());
    }

    // push message to db and update action of this gallery
    public void sendMedia(final MediaFB media) {
        Log.d(TAG, "Send MediaFB: " + media);

        final Uri file = Uri.fromFile(new File(media.getUri()));
        StorageReference imagesRef = mStorageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = imagesRef.putFile(file);


        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                final String stringUri;
                stringUri = downloadUrl.toString();
                media.setUri(stringUri);

                // push a message into mGalleryPhotos reference
                mGalleryPhotos.push().setValue(media);

                // update description and dataTime of action of this associated Gallery
                Map<String, Object> actionUpdates = new HashMap<>();
                //actionUpdates.put("description", photo.getText());
                actionUpdates.put("dataTime", media.getDateTime());
                mAction.updateChildren(actionUpdates);
            }
        });


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