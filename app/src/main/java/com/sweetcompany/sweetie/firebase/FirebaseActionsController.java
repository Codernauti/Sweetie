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
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.model.ChatFB;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.ToDoListFB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ghiro on 22/05/2017.
 */

public class FirebaseActionsController {

    private static final String TAG = "FbActionsController";

    private final DatabaseReference mDatabaseRef;
    private final DatabaseReference mActionsRef;
    /*private final DatabaseReference mChatsRef;
    private final DatabaseReference mGalleriesRef;
    private final DatabaseReference mToDoListsRef;*/

    private final String mActionsUrl;
    private final String mChatsUrl;
    private final String mChatMessagesUrl;
    private final String mGalleriesUrl;
    private final String mGalleryPhotosUrl;
    private final String mGeogiftsUrl;
    private final String mToDoListsUrl;
    private final String mToDoListCheckEntries;

    private ValueEventListener mActionsEventListener;

    private List<OnFirebaseActionsDataChange> mListeners = new ArrayList<>();
    private StorageReference mCoupleStorage;

    public interface OnFirebaseActionsDataChange {
        void onActionsListChanged(List<ActionFB> actions);
    }


    public FirebaseActionsController(String coupleUid, String userUid) {
        mActionsUrl = Constraints.ACTIONS + "/" + coupleUid;

        mChatsUrl = Constraints.CHATS + "/" + coupleUid;
        mChatMessagesUrl = Constraints.CHAT_MESSAGES + "/" + coupleUid;

        mGalleriesUrl = Constraints.GALLERIES + "/" + coupleUid;
        mGalleryPhotosUrl = Constraints.GALLERY_PHOTOS + "/" + coupleUid;

        mGeogiftsUrl = Constraints.GEOGIFTS + "/" + coupleUid;

        mToDoListsUrl = Constraints.TODOLIST + "/" + coupleUid;
        mToDoListCheckEntries = Constraints.TODOLIST_CHECKENTRY + "/" + coupleUid;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mActionsRef = mDatabaseRef.child(mActionsUrl);
        /*mChatsRef = mDatabaseRef.child(mChatsUrl);
        mGalleriesRef = mDatabaseRef.child(mGalleriesUrl);
        mToDoListsRef = mDatabaseRef.child(mToDoListsUrl);*/

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mCoupleStorage = mStorageRef.child(Constraints.ACTIONS_IMAGES + "/" + coupleUid);
    }

    public void addListener(OnFirebaseActionsDataChange listener) {
        mListeners.add(listener);
    }

    public void removeListener(OnFirebaseActionsDataChange listener) {
        mListeners.remove(listener);
    }

    public void attachNetworkDatabase() {
        if (mActionsEventListener == null) {
            mActionsEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot actionsSnapshot) {
                    List<ActionFB> actions = new ArrayList<>();

                    for (DataSnapshot actionSnapshot : actionsSnapshot.getChildren()) {
                        String actionKey = actionSnapshot.getKey();
                        Log.d("actions_Key", actionKey); // key of Actions FB Database

                        ActionFB action = actionSnapshot.getValue(ActionFB.class);
                        action.setKey(actionKey);
                        actions.add(action);
                    }

                    for (OnFirebaseActionsDataChange listener : mListeners) {
                        listener.onActionsListChanged(actions);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }
            };

            // TODO test much more sorting
            mActionsRef.orderByChild("dataTime").addValueEventListener(mActionsEventListener);
        }
    }

    public void detachNetworkDatabase() {
        if (mActionsEventListener != null) {
            mActionsRef.removeEventListener(mActionsEventListener);
        }
        mActionsEventListener = null;
    }

    public List<String> pushAction(ActionFB action) {
        List<String> newKeys = new ArrayList<>();

        // info share between parentAction and childAction
        String actionUid = mActionsRef.push().getKey();

        HashMap<String, Object> updates = new HashMap<>();

        // init child action
        switch (action.getType()) {
            case ActionFB.CHAT: {
                ChatFB chat = new ChatFB();
                chat.setTitle(action.getTitle());
                chat.setDate(action.getDataTime());

                updates.put(mChatsUrl + "/" + actionUid, chat);
                break;
            }
            case ActionFB.GALLERY: {
                GalleryFB gallery = new GalleryFB();
                gallery.setTitle(action.getTitle());
                gallery.setDate(action.getDataTime());

                updates.put(mGalleriesUrl + "/" + actionUid, gallery);
                break;
            }
            case ActionFB.TODOLIST: {
                ToDoListFB toDoList = new ToDoListFB();
                toDoList.setTitle(action.getTitle());
                toDoList.setDate(action.getDataTime());

                updates.put(mToDoListsUrl + "/" + actionUid, toDoList);
                break;
            }
            case ActionFB.GEOGIFT:
            default:
                break;
        }

        // init action
        action.setChildKey(actionUid);
        updates.put(mActionsUrl + "/" + actionUid, action);

        // update database
        mDatabaseRef.updateChildren(updates);

        // keep the uid to return
        newKeys.add(actionUid);

        return newKeys;
    }

    public void removeAction(String actionUid, int childType, String childUid) {
        HashMap<String, Object> updates = new HashMap<>();

        // Remove action
        updates.put(mActionsUrl + "/" + actionUid, null);

        // Remove childAction (chat, galleries etc.)
        switch (childType) {
            case ActionFB.CHAT: {
                updates.put(mChatsUrl + "/" + childUid, null);
                updates.put(mChatMessagesUrl + "/" + childUid, null);
                break;
            }
            case ActionFB.GALLERY: {
                updates.put(mGalleriesUrl + "/" + childUid, null);
                updates.put(mGalleryPhotosUrl + "/" + childUid, null);
                break;
            }
            case ActionFB.GEOGIFT: {
                updates.put(mGeogiftsUrl + "/" + childUid, null);
                break;
            }
            case ActionFB.TODOLIST: {
                updates.put(mToDoListsUrl + "/" + childUid, null);
                updates.put(mToDoListCheckEntries + "/" + childUid, null);
                break;
            }
            default:
                Log.d(TAG, "Cannot remove action, removing of this type is not supported " + childType);
                break;
        }

        mDatabaseRef.updateChildren(updates);
    }

    /**
     * Change image of an action with FirebaseActionInfoController
     * @param actionUid
     * @param imgLocalUri
     */
    @Deprecated
    public void uploadImage(final String actionUid, Uri imgLocalUri) {
        // set name of file
        StorageReference fileRef = mCoupleStorage.child(imgLocalUri.getLastPathSegment());
        // upload image
        UploadTask uploadTask = fileRef.putFile(imgLocalUri);
        // set listeners
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure sendFileFirebase " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess sendImage(): " + taskSnapshot.getDownloadUrl() + "\n" +
                        "update into uri: " + mActionsRef);

                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                mActionsRef.child(actionUid + "/" + Constraints.Actions.IMAGE_URL).setValue(downloadUrl.toString());

                /*String imageStorageUriString = downloadUrl.toString();

                HashMap<String, Object> updates = new HashMap<>();
                updates.put(mActionsUrl + "/" + actionUid + "/" + Constraints.Actions.IMAGE_URL, imageStorageUriString);
                // Here we can't have childActionUid
                // updates.put(mChildActionUrl + "/" + Constraints.ChildAction.URI_COVER, imageStorageUriString);

                mDatabaseRef.updateChildren(updates);*/


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                Log.d(TAG, "Upload image progress: " + progress);

                /*if (mListener != null) {
                    mListener.onImageUploadProgress((int) progress);
                }*/
            }
        });
    }

}