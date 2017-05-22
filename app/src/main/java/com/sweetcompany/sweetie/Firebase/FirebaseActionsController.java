package com.sweetcompany.sweetie.Firebase;

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

    private static final String TAG = "FireBActionsController";

    private final DatabaseReference mActionsDbReference;

    private static FirebaseActionsController mInstance;
    private List<OnFirebaseActionsDataChange> mListeners;
    private ValueEventListener mActionsEventListener;


    public interface OnFirebaseActionsDataChange {
        void updateActionsList(List<ActionFB> actions);
    }


    private FirebaseActionsController() {
        mListeners = new ArrayList<>();
        mActionsDbReference = FirebaseDatabase.getInstance()
                                           .getReference().child("actions");
    }

    public static FirebaseActionsController getInstance() {
        if (mInstance == null) {
            mInstance = new FirebaseActionsController();
        }
        return mInstance;
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
                        ActionFB action = actionSnapshot.getValue(ActionFB.class);
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

            mActionsDbReference.addValueEventListener(mActionsEventListener);
        }
    }

    public void detachNetworkDatabase() {
        if (mActionsEventListener != null) {
            mActionsDbReference.removeEventListener(mActionsEventListener);
        }
        mActionsEventListener = null;
    }

    public void pushMessage(Message msg) {
        DatabaseReference newMessagePush = mActionsDbReference.push();
        newMessagePush.setValue(msg);
    }

}