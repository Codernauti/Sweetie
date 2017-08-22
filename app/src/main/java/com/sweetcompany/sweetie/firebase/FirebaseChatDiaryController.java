package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sweetcompany.sweetie.model.MessageFB;

/**
 * Created by Eduard on 22-Aug-17.
 */

public class FirebaseChatDiaryController {

    private static final String TAG = "ChatDiaryController";

    private DatabaseReference mActionDiary;
    private ChildEventListener mActionDiaryListener;

    private Listener mListener;

    public interface Listener {
        void onMessageAdded(MessageFB message);
        void onMessageRemoved(MessageFB message);
    }

    public FirebaseChatDiaryController(String coupleUid, String actionDiaryDate, String actionDiaryUid) {
        mActionDiary = FirebaseDatabase.getInstance().getReference(
                        Constraints.ACTIONS_DIARY + "/"
                        + coupleUid + "/"
                        + actionDiaryDate + "/"
                        + actionDiaryUid);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void attachListener() {
        if (mActionDiaryListener == null) {
            mActionDiaryListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MessageFB newMessage = dataSnapshot.getValue(MessageFB.class);
                    newMessage.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onMessageAdded to chat: " + newMessage.getText());

                    if (mListener != null) {
                        mListener.onMessageAdded(newMessage);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    MessageFB removedMessage = dataSnapshot.getValue(MessageFB.class);
                    Log.d(TAG, "onMessageRemoved from chat: " + removedMessage.getText());

                    if (mListener != null) {
                        mListener.onMessageRemoved(removedMessage);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mActionDiary.addChildEventListener(mActionDiaryListener);
        }
    }

    public void detachListener() {
        if (mActionDiaryListener != null) {
            mActionDiary.removeEventListener(mActionDiaryListener);
        }
        mActionDiaryListener = null;
    }


}
