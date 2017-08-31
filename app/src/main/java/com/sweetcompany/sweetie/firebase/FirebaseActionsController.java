package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.model.ChatFB;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.GeogiftFB;
import com.sweetcompany.sweetie.model.ToDoListFB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ghiro on 22/05/2017.
 */

public class FirebaseActionsController {

    private static final String TAG = "FbActionsController";

    private final DatabaseReference mDatabaseRef;
    private final DatabaseReference mActionsRef;
    private final DatabaseReference mChatsRef;
    private final DatabaseReference mGalleriesRef;
    private final DatabaseReference mToDoListsRef;

    private final String mActionsUrl;
    private final String mChatsUrl;
    private final String mChatMessagesUrl;
    private final String mGalleriesUrl;
    private final String mGalleryPhotosUrl;
    private final String mGeogiftsUrl;
    private final String mToDoListsUrl;
    private final String mToDoListCheckEntries;

    private List<OnFirebaseActionsDataChange> mListeners = new ArrayList<>();
    private ValueEventListener mActionsEventListener;

    public interface OnFirebaseActionsDataChange {
        void updateActionsList(List<ActionFB> actions);
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
        mChatsRef = mDatabaseRef.child(mChatsUrl);
        mGalleriesRef = mDatabaseRef.child(mGalleriesUrl);
        mToDoListsRef = mDatabaseRef.child(mToDoListsUrl);
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
                        listener.updateActionsList(actions);
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
        String childActionUid = "";
        String childTitle = action.getTitle();

        HashMap<String, Object> updates = new HashMap<>();

        // init child action
        switch (action.getType()) {
            case ActionFB.CHAT: {
                ChatFB chat = new ChatFB();
                chat.setTitle(childTitle);
                DatabaseReference childActionPush = mChatsRef.push();
                childActionUid = childActionPush.getKey();

                updates.put(mChatsUrl + "/" + childActionUid, chat);
                break;
            }
            case ActionFB.GALLERY: {
                GalleryFB gallery = new GalleryFB();
                gallery.setTitle(childTitle);
                // TODO
                gallery.setLatitude("null");
                gallery.setLongitude("null");
                gallery.setUriCover("null");

                DatabaseReference childActionPush = mGalleriesRef.push();
                childActionUid = childActionPush.getKey();
                updates.put(mGalleriesUrl + "/" + childActionUid, gallery);
                break;
            }
            case ActionFB.TODOLIST: {
                ToDoListFB toDoList = new ToDoListFB();
                toDoList.setTitle(childTitle);

                DatabaseReference childActionPush = mToDoListsRef.push();
                childActionUid = childActionPush.getKey();
                updates.put(mToDoListsUrl + "/" + childActionUid, toDoList);
                break;
            }
            case ActionFB.GEOGIFT:
            default:
                break;
        }

        DatabaseReference actionPush = mActionsRef.push();
        String actionUid = actionPush.getKey();

        // init action
        action.setChildKey(childActionUid);
        updates.put(mActionsUrl + "/" + actionUid, action);

        // update database
        mDatabaseRef.updateChildren(updates);

        // keep the uid to return
        newKeys.add(childActionUid);
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

}