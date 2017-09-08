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
import com.sweetcompany.sweetie.model.ChatFB;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.ToDoListFB;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.util.HashMap;

/**
 * Created by Eduard on 04-Sep-17.
 */

public class FirebaseActionInfoController<AT> {

    private static final String TAG = "ActionInfoController";

    private final DatabaseReference mDatabaseRef;

    private final StorageReference mActionsImgStorageRef;
    private final DatabaseReference mActionRef;
    private ValueEventListener mActionListener;

    private final String mParentActionUrl;
    private final String mActionUrl;

    private final Class<AT> mActionObjClass;
    private final String mActionUid;


    private Listener mListener;

    public interface Listener<AT> {
        void onActionInfoChanged(AT actionFB);
    }


    public FirebaseActionInfoController(String coupleUid, String actionUid, Class<AT> actionObjClass) {
        mActionObjClass = actionObjClass;
        mActionUid = actionUid;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mActionsImgStorageRef = FirebaseStorage.getInstance().getReference(Constraints.ACTIONS_IMAGES + "/" + coupleUid);

        mParentActionUrl = Constraints.ACTIONS + "/" + coupleUid + "/" + actionUid;

        if (actionObjClass.isAssignableFrom(GalleryFB.class)) {
            mActionUrl = Constraints.GALLERIES + "/" + coupleUid + "/" + actionUid;
        } else if (actionObjClass.isAssignableFrom(ChatFB.class)) {
            mActionUrl = Constraints.CHATS + "/" + coupleUid + "/" + actionUid;
        } else if (actionObjClass.isAssignableFrom(ToDoListFB.class)) {
            mActionUrl = Constraints.TODOLIST + "/" + coupleUid + "/" + actionUid;
        } else {
            mActionUrl = "";
            Log.w(TAG, "actionObjClass assigned is not managed by this class");
        }

        mActionRef = mDatabaseRef.child(mActionUrl);
    }


    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void attachListener() {
        if (mActionListener == null) {
            mActionListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataInfoChange trigger!");
                    AT action = dataSnapshot.getValue(mActionObjClass);

                    if (mListener != null && action != null) {
                        Log.d(TAG, action.toString());
                        mListener.onActionInfoChanged(action);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mActionRef.addValueEventListener(mActionListener);
        }
    }

    public void detachListener() {
        if (mActionListener != null) {
            mActionRef.removeEventListener(mActionListener);
        }
        mActionListener = null;
    }


    public void changeImage(final Uri imageLocalUri) {

        // update database
        mActionRef.child(Constraints.ChildAction.UPLOADING_IMG).setValue(true);

        String imgName = DataMaker.get_UTC_DateTime() + mActionUid;
        UploadTask uploadTask = mActionsImgStorageRef.child(imgName).putFile(imageLocalUri);

        uploadTask
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "onFailure sendFileFirebase " + exception.getMessage());
                        HashMap<String, Object> updates = new HashMap<>();
                        updates.put(mActionUrl + "/" + Constraints.ChildAction.PROGRESS, null);
                        updates.put(mActionUrl + "/" + Constraints.ChildAction.UPLOADING_IMG, false);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess sendImage(): " + taskSnapshot.getDownloadUrl() +"\n" + "update into uri: " + mActionUrl);
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        String imageStorageUriString = downloadUrl.toString();

                        HashMap<String, Object> updates = new HashMap<>();
                        updates.put(mActionUrl + "/" + Constraints.ChildAction.URI_COVER, imageStorageUriString);
                        updates.put(mActionUrl + "/" + Constraints.ChildAction.PROGRESS, null);
                        updates.put(mActionUrl + "/" + Constraints.ChildAction.UPLOADING_IMG, false);
                        if (mActionObjClass == GalleryFB.class) {
                            updates.put(mActionUrl + "/" + Constraints.Galleries.IMG_SET_BY_USER, true);
                        }
                        updates.put(mParentActionUrl + "/" + Constraints.Actions.IMAGE_URL, imageStorageUriString);

                        mDatabaseRef.updateChildren(updates);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        Log.d(TAG, "Upload image progress: " + progress);

                        mActionRef.child(Constraints.ChildAction.PROGRESS).setValue(progress);
                    }
                });
    }

    public void changeTitle(String newTitle) {
        HashMap<String, Object> updates = new HashMap<>();

        updates.put(mActionUrl + "/" + Constraints.ChildAction.TITLE, newTitle);
        updates.put(mParentActionUrl + "/" + Constraints.Actions.TITLE, newTitle);

        mDatabaseRef.updateChildren(updates);
    }

    public void changePosition(Double latitude, Double longitude) {
        if (mActionObjClass == GalleryFB.class) {
            HashMap<String, Object> updates = new HashMap<>();

            updates.put(mActionUrl + "/" + Constraints.Galleries.LATITUDE, latitude);
            updates.put(mActionUrl + "/" + Constraints.Galleries.LONGITUDE, longitude);

            mDatabaseRef.updateChildren(updates);
        }
    }
}
