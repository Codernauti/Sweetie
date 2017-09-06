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
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.MediaFB;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.util.ArrayList;
import java.util.Calendar;
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
            mGalleryPhotos.orderByChild("dataTime").addChildEventListener(mGalleryPhotosListener);
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


    private void addMedia(String mediaUid, MediaFB media){
        Map<String, Object> updates = new HashMap<>();

        // push media
        updates.put(mGalleryPhotosUrl + "/" + mediaUid, media);

        // update parent action dateTime & imageUrl & partnerNotificationCounter
        updates.put(mActionUrl + "/" + Constraints.Actions.DESCRIPTION, "\uD83D\uDCF7");
        updates.put(mActionUrl + "/" + Constraints.Actions.DATE_TIME, media.getDateTime());

        if (!mIsImageSetByUser) {
            // update photo cover gallery and parent action
            updates.put(mActionUrl + "/" + Constraints.Actions.IMAGE_URL, media.getUriStorage());
            updates.put(mGalleryUrl + "/" + Constraints.Galleries.URI_COVER, media.getUriStorage());
        }

        super.updateNotificationCounter(updates);

        mDatabaseRef.updateChildren(updates);
    }

    // push message to db and update action of this gallery
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

                addMedia(newMediaUID, media);
            }
        }).addOnProgressListener(
                new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        Log.d(TAG, taskSnapshot.toString() + " onProgress: " + progress);

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
        mMediaRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "media is deleted");
                mGalleryPhotos.child(mediaUid).removeValue();
            }
        });
    }


}