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
import com.sweetcompany.sweetie.model.UserFB;
import com.sweetcompany.sweetie.settings.SettingsPresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eduard on 28-Aug-17.
 */

public class FirebaseSettingsController {

    private static final String TAG = "SettingsControler";

    private final String mUserUid;
    private final String mUserUrl;

    private final DatabaseReference mUserRef;
    private ValueEventListener mUserRefListener;

    private final StorageReference mUserStorage;

    private SettingsControllerListener mListener;

    public interface SettingsControllerListener {
        void onUserChanged(UserFB user);
        void onImageUploadProgress(int progress);
    }

    public FirebaseSettingsController(String userUid) {
        mUserUid = userUid;
        mUserUrl = Constraints.USERS + "/" + mUserUid;

        mUserStorage = FirebaseStorage.getInstance().getReference(Constraints.USERS);

        mUserRef = FirebaseDatabase.getInstance().getReference(mUserUrl);
    }

    public void setListener(SettingsControllerListener listener) {
        mListener = listener;
    }

    public void attachListener() {
        if (mUserRefListener == null) {
            mUserRefListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserFB user = dataSnapshot.getValue(UserFB.class);

                    if (mListener != null) {
                        mListener.onUserChanged(user);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mUserRef.addValueEventListener(mUserRefListener);
        }
    }

    public void detachListener() {
        if (mUserRefListener != null) {
            mUserRef.removeEventListener(mUserRefListener);
        }
        mUserRefListener = null;
    }

    public void changeUserImage(final Uri imageLocalUri) {
        // TODO: duplicated code with ChatController
        StorageReference photoRef = mUserStorage.child(imageLocalUri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(imageLocalUri);
        final String imageLocalUriString = imageLocalUri.toString();

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure sendFileFirebase " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess sendImage(): " + taskSnapshot.getDownloadUrl());
                /*Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String imageStorageUriString = downloadUrl.toString();

                Map<String, Object> updates = new HashMap<>();
                updates.put(Constraints.IMAGE_LOCAL_URI, imageLocalUriString);
                updates.put(Constraints.IMAGE_STORAGE_URI, imageStorageUriString);

                mCouple.updateChildren(updates);*/
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
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
}
