package com.sweetcompany.sweetie.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.HashMap;

@Deprecated
public class FirebaseGalleryInfoController {

    private static final String TAG = "GalleryInfoController";

    private final DatabaseReference mDatabaseRef;

    private final StorageReference mActionsImgStorageRef;
    private final DatabaseReference mGalleryRef;
    private ValueEventListener mGalleryListener;

    private final String mParentActionUrl;
    private final String mGalleryUrl;


    private Listener mListener;

    public interface Listener {
        void onGalleryInfoChanged(GalleryFB actionFB);
        void onImageUploadProgress(int progress);
    }


    /**
     *  @Deprecated use FirebaseActionInfoController instead
     */
    public FirebaseGalleryInfoController(String coupleUid, String galleryUid, String parentActionUid) {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mActionsImgStorageRef = FirebaseStorage.getInstance().getReference(Constraints.ACTIONS_IMAGES + "/" + coupleUid);

        mParentActionUrl = Constraints.ACTIONS + "/" + coupleUid + "/" + parentActionUid;
        mGalleryUrl = Constraints.GALLERIES + "/" + coupleUid + "/" + galleryUid;

        mGalleryRef = mDatabaseRef.child(mGalleryUrl);
    }


    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void attachCoupleListener() {
        if (mGalleryListener == null) {
            mGalleryListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataInfoChange trigger!");
                    GalleryFB action = dataSnapshot.getValue(GalleryFB.class);

                    if (mListener != null && action != null) {
                        mListener.onGalleryInfoChanged(action);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mGalleryRef.addValueEventListener(mGalleryListener);
        }
    }

    public void detachCoupleListener() {
        if (mGalleryListener != null) {
            mGalleryRef.removeEventListener(mGalleryListener);
        }
        mGalleryListener = null;
    }


    public void changeImage(final Uri imageLocalUri) {
        StorageReference photoRef = mActionsImgStorageRef.child(imageLocalUri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(imageLocalUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e(TAG, "onFailure sendFileFirebase " + exception.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess sendImage(): " + taskSnapshot.getDownloadUrl() +"\n" + "update into uri: " + mGalleryUrl);
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        String imageStorageUriString = downloadUrl.toString();

                        HashMap<String, Object> updates = new HashMap<>();
                        updates.put(mGalleryUrl + "/" + Constraints.Galleries.URI_COVER, imageStorageUriString);
                        updates.put(mGalleryUrl + "/" + Constraints.Galleries.IMG_SET_BY_USER, true);
                        updates.put(mParentActionUrl + "/" + Constraints.Actions.IMAGE_URL, imageStorageUriString);

                        mDatabaseRef.updateChildren(updates);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    Log.d(TAG, "Upload image progress: " + progress);

                    if (mListener != null) {
                        mListener.onImageUploadProgress((int) progress);
                    }
                }
            });
    }

    public void changeTitle(String newTitle) {
        HashMap<String, Object> updates = new HashMap<>();

        updates.put(mGalleryUrl + "/" + Constraints.Galleries.TITLE, newTitle);
        updates.put(mParentActionUrl + "/" + Constraints.Actions.TITLE, newTitle);

        mDatabaseRef.updateChildren(updates);
    }
}
