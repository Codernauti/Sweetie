package com.sweetcompany.sweetie.firebase;

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
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.MediaFB;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ghiro on 22/07/2017.
 */

public class FirebaseGalleryController extends FirebaseGeneralActionController {

    private static final String TAG = "FbGalleryController";

    private final String mCoupleUid;

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


    public FirebaseGalleryController(String coupleUid, String galleryKey, String actionUid,
                                     String userUid, String partnerUid) {
        super(coupleUid, userUid, partnerUid, actionUid);

        mCoupleUid = coupleUid;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mGalleryUrl = Constraints.GALLERIES + "/" + coupleUid + "/" + galleryKey;
        mGalleryPhotosUrl = Constraints.GALLERY_PHOTOS + "/" + coupleUid + "/" + galleryKey;
        mActionUrl = Constraints.ACTIONS + "/" + coupleUid + "/" + actionUid;

        mGallery = mDatabaseRef.child(mGalleryUrl);
        mGalleryPhotos = mDatabaseRef.child(mGalleryPhotosUrl);

        String mMediaGalleryUrl = Constraints.GALLERY_PHOTOS_DIRECTORY + "/" + mCoupleUid;
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
            mGalleryPhotos.orderByChild(Constraints.GalleryPhotos.LAST_UPDATE_DATE).addChildEventListener(mGalleryPhotosListener);
        }

        if (mGalleryListener == null) {
            mGalleryListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // TODO: test
                    GalleryFB gallery = dataSnapshot.getValue(GalleryFB.class);
                    gallery.setKey(dataSnapshot.getKey());

                    mIsImageSetByUser = gallery.isImageSetByUser();

                    for (GalleryControllerListener listener : mListeners) {
                        listener.onGalleryChanged(gallery);
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


    @Deprecated
    public String sendMedia(final MediaFB media) {
        Log.d(TAG, "Send MediaFB: " + media);

        final String newMediaUID = mGalleryPhotos.push().getKey();

        Uri uriLocal = Uri.parse(media.getUriStorage());
        StorageReference photoRef = mMediaGallery.child( DataMaker.get_UTC_DateTime() + newMediaUID );
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

                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                media.setUriStorage(downloadUrl.toString());

                addMediaToDatabase(newMediaUID, media);
            }
        }).addOnProgressListener(
                new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        Log.d(TAG, taskSnapshot.toString() + " onProgress: " + progress);

                        /*for (GalleryControllerListener listener : mListeners) {
                            listener.onUploadPercent(media, ((int) progress));
                        }*/
                    }
                });

        return newMediaUID;
    }

    public void uploadMedia(final MediaFB media) {
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
            StorageReference mMediaRef = FirebaseStorage.getInstance().getReferenceFromUrl(uriStorage);
            mMediaRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "media is deleted from storage");
                    removeMediaFromDatabase(mediaUid);
                    //mGalleryPhotos.child(mediaUid).removeValue();
                }
            });
        }
    }

    private void removeMediaFromDatabase(final String mediaUid) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(mGalleryPhotosUrl + "/" + mediaUid, null);
        super.updateNotificationCounterAfterDeletion(updates, mediaUid);

        mDatabaseRef.updateChildren(updates);
    }


}