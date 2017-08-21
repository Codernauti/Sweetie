package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.model.ChatFB;
import com.sweetcompany.sweetie.model.GalleryFB;
import com.sweetcompany.sweetie.model.GeogiftFB;
import com.sweetcompany.sweetie.model.ToDoListFB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 22/05/2017.
 */

public class FirebaseActionsController {

    private static final String TAG = "FbActionsController";

    private final DatabaseReference mActionsDbReference;
    private final DatabaseReference mChatsDbReference;
    private final DatabaseReference mGalleriesDbReference;
    private final DatabaseReference mToDoListsDbReference;
    private final DatabaseReference mGeogiftDbReference;

    private List<OnFirebaseActionsDataChange> mListeners = new ArrayList<>();
    private ValueEventListener mActionsEventListener;

    private String userID;

    public interface OnFirebaseActionsDataChange {
        void updateActionsList(List<ActionFB> actions);
        void onRetrievedGeogift(List<GeogiftFB> geogifts);
    }


    public FirebaseActionsController(String coupleUid, String userUid) {
        mActionsDbReference = FirebaseDatabase.getInstance().getReference(Constraints.ACTIONS + "/" + coupleUid);
        mChatsDbReference = FirebaseDatabase.getInstance().getReference(Constraints.CHATS + "/" + coupleUid);
        mGalleriesDbReference = FirebaseDatabase.getInstance().getReference(Constraints.GALLERIES + "/" + coupleUid);
        mToDoListsDbReference = FirebaseDatabase.getInstance().getReference(Constraints.TODOLIST + "/" + coupleUid);
        mGeogiftDbReference = FirebaseDatabase.getInstance().getReference(Constraints.GEOGIFT + "/" + coupleUid);
        userID = userUid;
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
            mActionsDbReference.orderByChild("dataTime").addValueEventListener(mActionsEventListener);
        }
    }

    public void detachNetworkDatabase() {
        if (mActionsEventListener != null) {
            mActionsDbReference.removeEventListener(mActionsEventListener);
        }
        mActionsEventListener = null;
    }

    public void retrieveGeogiftFB(){

        final ArrayList<GeogiftFB> geogiftFBlist = new ArrayList<>();
        mGeogiftDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w(TAG, "geogiftFB retrieving");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GeogiftFB geogiftFB = snapshot.getValue(GeogiftFB.class);
                    if(!geogiftFB.isVisited() && !geogiftFB.getUserCreatorUID().equals(userID)) {
                        geogiftFBlist.add(geogiftFB);
                    }
                }

                for (OnFirebaseActionsDataChange listener : mListeners) {
                    listener.onRetrievedGeogift(geogiftFBlist);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public void pushAction(ActionFB act) {
        DatabaseReference newActionPush = mActionsDbReference.push();
        newActionPush.setValue(act);
    }

    public List<String> pushChatAction(ActionFB actionFB, String chatTitle) {
        List<String> newKeys =  new ArrayList<String>();

        DatabaseReference newChatPush = mChatsDbReference.push();
        String newChatKey = newChatPush.getKey();
        newKeys.add(newChatKey);
        DatabaseReference newActionPush = mActionsDbReference.push();
        String newActionKey = newActionPush.getKey();
        newKeys.add(newActionKey);

        // Set Action
        actionFB.setChildKey(newChatKey);

        // Create Chat and set Chat
        ChatFB chat = new ChatFB();
        chat.setTitle(chatTitle);

        // put into queue for network
        newChatPush.setValue(chat);
        newActionPush.setValue(actionFB);

        return newKeys;
    }

    public List<String> pushGalleryAction(ActionFB actionFB, String galleryTitle) {
        List<String> newKeys =  new ArrayList<String>();

        DatabaseReference newGalleryPush = mGalleriesDbReference.push();
        String newGalleryKey = newGalleryPush.getKey();
        newKeys.add(newGalleryKey);
        DatabaseReference newActionPush = mActionsDbReference.push();
        String newActionKey = newActionPush.getKey();
        newKeys.add(newActionKey);

        // Set Action
        actionFB.setChildKey(newGalleryKey);

        // Create Gallery and set Gallery
        GalleryFB gallery = new GalleryFB();
        gallery.setTitle(galleryTitle);

        // put into queue for network
        newGalleryPush.setValue(gallery);
        newActionPush.setValue(actionFB);

        return newKeys;
    }

    public List<String> pushToDoListAction(ActionFB actionFB, String todolistTitle) {
        List<String> newKeys =  new ArrayList<String>();

        DatabaseReference newToDoListPush = mToDoListsDbReference.push();
        String newToDoListKey = newToDoListPush.getKey();
        newKeys.add(newToDoListKey);
        DatabaseReference newActionPush = mActionsDbReference.push();
        String newActionKey = newActionPush.getKey();
        newKeys.add(newActionKey);

        // Set Action
        actionFB.setChildKey(newToDoListKey);

        // Create ToDoList and set ToDoList
        ToDoListFB toDoList = new ToDoListFB();
        toDoList.setTitle(todolistTitle);

        // put into queue for network
        newToDoListPush.setValue(toDoList);
        newActionPush.setValue(actionFB);

        return newKeys;
    }

    /*public List<String> pushGeogiftAction(ActionFB actionFB, String geogiftTitle) {
        List<String> newKeys =  new ArrayList<String>();

        DatabaseReference newGeogiftPush = mGeogiftDbReference.push();
        String newGeogiftKey = newGeogiftPush.getKey();
        newKeys.add(newGeogiftKey);
        DatabaseReference newActionPush = mActionsDbReference.push();
        String newActionKey = newActionPush.getKey();
        newKeys.add(newActionKey);

        // Set Action
        actionFB.setChildKey(newGeogiftKey);

        // Create Gallery and set Gallery
        GeogiftFB geogift = new GeogiftFB();

        // put into queue for network
        newGeogiftPush.setValue(geogift);
        newActionPush.setValue(actionFB);

        return newKeys;
    }*/

}