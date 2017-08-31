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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.MediaFB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ghiro on 22/07/2017.
 */

public class FirebaseGalleryController {

    private static final String TAG = "FbGalleryController";

    private final String mCoupleUid;

    private final DatabaseReference mDatabaseRef;
    private final StorageReference mStorageRef;

    private final DatabaseReference mGallery;
    private final DatabaseReference mGalleryPhotos;
    private final StorageReference mMediaGallery;

    private final String mGalleryUrl;           // galleries/<couple_uid>/<gallery_uid>
    private final String mGalleryPhotosUrl;     // gallery-photos/<couple_uid>/<gallery_uid>
    private final String mActionUrl;           // actions/<couple_uid>/<action_uid>

    private ValueEventListener mGalleryListener;
    private ChildEventListener mGalleryPhotosListener;

    private List<GalleryControllerListener> mListeners = new ArrayList<>();

    public interface GalleryControllerListener {
        void onGalleryChanged(GalleryFB gallery);

        void onMediaAdded(MediaFB media);
        void onMediaRemoved(MediaFB media);
        void onMediaChanged(MediaFB media);
        void onUploadPercent(MediaFB media, int perc);
    }


    public FirebaseGalleryController(String coupleUid, String galleryKey, String actionKey) {
        mCoupleUid = coupleUid;

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mGalleryUrl = Constraints.GALLERIES + "/" + coupleUid + "/" + galleryKey;
        mGalleryPhotosUrl = Constraints.GALLERY_PHOTOS + "/" + coupleUid + "/" + galleryKey;
        mActionUrl = Constraints.ACTIONS + "/" + coupleUid + "/" + actionKey;

        mGallery = mDatabaseRef.child(mGalleryUrl);
        mGalleryPhotos = mDatabaseRef.child(mGalleryPhotosUrl);

        String mMediaGalleryUrl = Constraints.GALLERY_PHOTOS_DIRECTORY + "/" + mCoupleUid;
        mMediaGallery = mStorageRef.child(mMediaGalleryUrl);
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
                    Log.d(TAG, "onChildAdded of gallery: " + newMedia.getUriStorage());

                    for (GalleryControllerListener listener : mListeners) {
                        listener.onMediaAdded(newMedia);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    MediaFB newMedia = dataSnapshot.getValue(MediaFB.class);
                    newMedia.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onChildChanged of gallery: " + newMedia.getUriStorage());

                    for (GalleryControllerListener listener : mListeners) {
                        listener.onMediaChanged(newMedia);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    MediaFB removedMedia = dataSnapshot.getValue(MediaFB.class);
                    removedMedia.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onChildRemoved of gallery: " + removedMedia.getUriStorage());

                    for (GalleryControllerListener listener : mListeners) {
                        listener.onMediaRemoved(removedMedia);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            mGalleryPhotos.orderByChild("dataTime").addChildEventListener(mGalleryPhotosListener);
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

        if (mGalleryPhotosListener != null) {
            mGalleryPhotos.removeEventListener(mGalleryPhotosListener);
        }
        mGalleryPhotosListener = null;
    }


    public void updateMedia(MediaFB photo) {
        Log.d(TAG, "Update MediaFB: " + photo);

        //DatabaseReference ref = mGalleryPhotos.child(photo.getKey()).child("bookmarked");
        //ref.setValue(photo.isBookmarked());
    }

    // push message to db and update action of this gallery
    public String sendMedia(final MediaFB media) {
        Log.d(TAG, "Send MediaFB: " + media);

        final String newMediaUID = mGalleryPhotos.push().getKey();

        Uri uriLocal = Uri.parse(media.getUriLocal());
        StorageReference photoRef = mMediaGallery.child(uriLocal.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(uriLocal);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e(TAG, "onFailure sendFileFirebase " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                final String stringUriStorage = downloadUrl.toString();
                media.setUriStorage(stringUriStorage);

                Map<String, Object> updates = new HashMap<>();

                // push media
                String newMediaUid = mGalleryPhotos.push().getKey();
                updates.put(mGalleryPhotosUrl + "/" + newMediaUid, media);

                // update parent action dateTime & imageUrl
                updates.put(mActionUrl + "/" + Constraints.Actions.DATE_TIME, media.getDateTime());
                updates.put(mActionUrl + "/" + Constraints.Actions.IMAGE_URL, media.getUriStorage());

                // update photo cover gallery
                updates.put(mGalleryUrl + "/" + Constraints.URI_COVER, media.getUriStorage());

                mDatabaseRef.updateChildren(updates);
            }
        }).addOnProgressListener(
                new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        for (GalleryControllerListener listener : mListeners) {
                            listener.onUploadPercent(media, ((int) progress));
                        }
                    }
                });

        return newMediaUID;
    }

    public void removeMedia(final String mediaUid, String uriStorage) {
        Log.d(TAG, "remove media:" + uriStorage);

        StorageReference mMediaRef = FirebaseStorage.getInstance().getReferenceFromUrl(uriStorage);
        mMediaRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mGalleryPhotos.child(mediaUid).removeValue();
            }
        });
    }


}