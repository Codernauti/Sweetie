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

import java.util.HashMap;

/**
 * Created by Eduard on 28-Aug-17.
 */

public class FirebaseSettingsController {

    private static final String TAG = "SettingsController";

    private final DatabaseReference mUserRef;
    private ValueEventListener mUserRefListener;

    private final StorageReference mUserStorage;

    private SettingsControllerListener mListener;

    public interface SettingsControllerListener extends ImageUploader.OnImageUploadProgressListener {
        void onUserChanged(UserFB user);
    }

    public FirebaseSettingsController(String userUid) {
        String mUserUrl = Constraints.USERS + "/" + userUid;

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
        mUserRef.child(Constraints.Users.UPLOADING_IMG).setValue(true);


        StorageReference photoStorageRef = mUserStorage.child(imageLocalUri.getLastPathSegment());

        UploadTask uploadTask = photoStorageRef.putFile(imageLocalUri);

        uploadTask
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mUserRef.child(Constraints.Users.UPLOADING_IMG).setValue(false);
                        Log.d(TAG, "onFailure upload user image");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String urlImg = taskSnapshot.getDownloadUrl().toString();

                        HashMap<String, Object> updates = new HashMap<String, Object>();
                        updates.put(Constraints.Users.UPLOADING_IMG, null);
                        updates.put(Constraints.Users.IMAGE_URL, urlImg);

                        mUserRef.updateChildren(updates);

                        Log.d(TAG, "Url image settings uploaded: " + urlImg);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        // TODO: implement it
                    }
                });
    }
}
