package com.codernauti.sweetie.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.codernauti.sweetie.model.GalleryFB;
import com.codernauti.sweetie.model.MediaFB;
import com.codernauti.sweetie.utils.DataMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseGalleryController extends FirebaseGeneralActionController {

    private static final String TAG = "FbGalleryController";

    private final String mGalleryUrl;           // galleries/<couple_uid>/<gallery_uid>
    private final String mGalleryPhotosUrl;     // gallery-photos/<couple_uid>/<gallery_uid>
    private final String mActionUrl;            // actions/<couple_uid>/<action_uid>

    private final DatabaseReference mDatabaseRef;

    private final DatabaseReference mGallery;
    private final DatabaseReference mGalleryPhotos;
    private final StorageReference mMediaGallery;

    private ValueEventListener mGalleryListener;
    private ChildEventListener mGalleryPhotosListener;

    private boolean mIsImageSetByUser;  // dirty bit

    private List<GalleryControllerListener> mListeners = new ArrayList<>();

    public interface GalleryControllerListener {
        void onGalleryChanged(GalleryFB gallery);

        void onMediaAdded(MediaFB media);
        void onMediaRemoved(MediaFB media);
        void onMediaChanged(MediaFB media);
        @Deprecated
        void onUploadPercent(MediaFB media, int perc);
    }


    public FirebaseGalleryController(String coupleUid, String actionUid, String userUid,
                                     String partnerUid) {
        super(coupleUid, userUid, partnerUid, actionUid);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mGalleryUrl = Constraints.GALLERIES + "/" + coupleUid + "/" + actionUid;
        mGalleryPhotosUrl = Constraints.GALLERY_PHOTOS + "/" + coupleUid + "/" + actionUid;
        mActionUrl = Constraints.ACTIONS + "/" + coupleUid + "/" + actionUid;

        mGallery = mDatabaseRef.child(mGalleryUrl);
        mGalleryPhotos = mDatabaseRef.child(mGalleryPhotosUrl);

        String mMediaGalleryUrl = Constraints.GALLERY_PHOTOS_DIRECTORY + "/" + coupleUid;
        mMediaGallery = FirebaseStorage.getInstance().getReference(mMediaGalleryUrl);
    }

    public void addListener(GalleryControllerListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(GalleryControllerListener listener) {
        mListeners.remove(listener);
    }


    public void attachListeners() {
        super.attachListeners();

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
            mGalleryPhotos.orderByChild(Constraints.GalleryPhotos.DATE_TIME).addChildEventListener(mGalleryPhotosListener);
        }

        if (mGalleryListener == null) {
            mGalleryListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GalleryFB gallery = dataSnapshot.getValue(GalleryFB.class);

                    if (gallery != null) {
                        gallery.setKey(dataSnapshot.getKey());

                        mIsImageSetByUser = gallery.isImageSetByUser();

                        for (GalleryControllerListener listener : mListeners) {
                            listener.onGalleryChanged(gallery);
                        }
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
        super.detachListeners();

        if (mGalleryListener != null) {
            mGallery.removeEventListener(mGalleryListener);
        }
        mGalleryListener = null;

        if (mGalleryPhotosListener != null) {
            mGalleryPhotos.removeEventListener(mGalleryPhotosListener);
        }
        mGalleryPhotosListener = null;
    }


    public void uploadMedia(final MediaFB media) {
        // TODO compress image
        updateMediaData(media);
        addMediaToDatabase(media.getKey(), media);
        addMediaToStorage(media);
    }

    private void updateMediaData(MediaFB media) {
        String mediaUid = mGalleryPhotos.push().getKey();

        media.setKey(mediaUid);
        media.setProgress(0);
        media.setUploading(true);
    }

    private void addMediaToDatabase(String mediaUid, MediaFB media){
        Map<String, Object> updates = new HashMap<>();

        // push media
        updates.put(mGalleryPhotosUrl + "/" + mediaUid, media);

        // update parent action dateTime & imageUrl & partnerNotificationCounter
        updates.put(mActionUrl + "/" + Constraints.Actions.DESCRIPTION, "\uD83D\uDCF7");
        updates.put(mActionUrl + "/" + Constraints.Actions.LAST_UPDATED_DATE, media.getDateTime());

        super.updateNotificationCounterAfterInsertion(updates, mediaUid);

        mDatabaseRef.updateChildren(updates);
    }

    private void addMediaToStorage(final MediaFB media) {
        String nameFile = DataMaker.get_UTC_DateTime() + media.getKey();
        Uri uriLocal = Uri.parse(media.getUriStorage());

        UploadTask uploadTask = mMediaGallery.child(nameFile).putFile(uriLocal);

        uploadTask
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "onFailure sendFileFirebase " + exception.getMessage());

                        removeMedia(media.getKey(), null);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        media.setUriStorage(downloadUrl.toString());

                        updateCompleteMedia(media);
                    }
                })
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                Log.d(TAG, taskSnapshot.toString() + " onProgress: " + progress);

                                // TODO: too many network request
                                /*mDatabaseRef.child(mGalleryPhotosUrl)
                                        .child(media.getKey())
                                        .child(Constraints.GalleryPhotos.PROGRESS)
                                        .setValue(progress);*/
                            }
                        });
    }

    private void updateCompleteMedia(final MediaFB media) {
        HashMap<String, Object> updates = new HashMap<>();

        // update media with complete data
        String mMediaUrl = mGalleryPhotosUrl + "/" + media.getKey();
        updates.put(mMediaUrl + "/" + Constraints.GalleryPhotos.UPLOADING, false);
        updates.put(mMediaUrl + "/" + Constraints.GalleryPhotos.PROGRESS, null);
        updates.put(mMediaUrl + "/" + Constraints.GalleryPhotos.URI_STORAGE, media.getUriStorage());

        if (!mIsImageSetByUser) {
            // update photo cover gallery and parent action
            updates.put(mActionUrl + "/" + Constraints.Actions.IMAGE_URL, media.getUriStorage());
            updates.put(mGalleryUrl + "/" + Constraints.Galleries.URI_COVER, media.getUriStorage());
        }

        mDatabaseRef.updateChildren(updates);
    }


    public void removeMedia(final String mediaUid, String uriStorage) {
        Log.d(TAG, "remove media:" + uriStorage);

        if (uriStorage == null) {
            removeMediaFromDatabase(mediaUid);
            /*mGalleryPhotos.child(mediaUid).removeValue();*/
        }
        else {
            try {
                StorageReference mMediaRef = FirebaseStorage.getInstance().getReferenceFromUrl(uriStorage);
                mMediaRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "media is deleted from storage");
                        removeMediaFromDatabase(mediaUid);
                        //mGalleryPhotos.child(mediaUid).removeValue();
                    }
                });
            } catch (IllegalArgumentException ex) {
                // TODO: bad code
                Log.d(TAG, "media not into storage");
                removeMediaFromDatabase(mediaUid);
            }
        }
    }

    private void removeMediaFromDatabase(final String mediaUid) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(mGalleryPhotosUrl + "/" + mediaUid, null);
        super.updateNotificationCounterAfterDeletion(updates, mediaUid);

        mDatabaseRef.updateChildren(updates);
    }


}