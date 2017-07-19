package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 22/05/2017.
 */

public class FirebaseActionsController {

    private static final String TAG = "FbActionsController";

    private final DatabaseReference mActionsDbReference;
    private final DatabaseReference mChatsDbReference;

    private List<OnFirebaseActionsDataChange> mListeners = new ArrayList<>();
    private ValueEventListener mActionsEventListener;

    public interface OnFirebaseActionsDataChange {
        void updateActionsList(List<ActionFB> actions);
    }


    public FirebaseActionsController(String coupleUid) {
        mActionsDbReference = FirebaseDatabase.getInstance().getReference(Constraints.ACTIONS_NODE + "/" + coupleUid);
        mChatsDbReference = FirebaseDatabase.getInstance().getReference(Constraints.CHATS_NODE + "/" + coupleUid);
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

}