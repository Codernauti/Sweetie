package com.sweetcompany.sweetie.firebase;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sweetcompany.sweetie.model.UserFB;

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

    public interface SettingsControllerListener extends ImageUploader.OnImageUploadProgressListener {
        void onUserChanged(UserFB user);
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
        StorageReference photoStorageRef = mUserStorage.child(imageLocalUri.getLastPathSegment());

        ImageUploader.build(photoStorageRef)
                .setDefaultImageUploadProgressListener(mListener)
                .setOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String urlImg = taskSnapshot.getDownloadUrl().toString();
                        mUserRef.child(Constraints.Users.IMAGE_URL).setValue(urlImg);

                        Log.d(TAG, "Url image settings uploaded: " + urlImg);
                    }
                })
                .startUpload(imageLocalUri);
    }
}
