package com.sweetcompany.sweetie.Firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eduard on 21-May-17.
 */

public class FirebaseChatController {

    private static final String TAG = "FirebaseChatController";

    private final DatabaseReference mChatDbReference;

    private static FirebaseChatController mInstance;
    private List<OnFirebaseChatDataChange> mListeners;
    private ValueEventListener mChatEventListener;


    public interface OnFirebaseChatDataChange {
        void notifyNewMessages(List<Message> messages);
    }


    private FirebaseChatController() {
        mListeners = new ArrayList<>();
        mChatDbReference = FirebaseDatabase.getInstance()
                                            .getReference().child("messages");
    }

    public static FirebaseChatController getInstance() {
        if (mInstance == null) {
            mInstance = new FirebaseChatController();
        }
        return mInstance;
    }

    public void addListener(OnFirebaseChatDataChange listener) {
        mListeners.add(listener);
    }

    public void removeListener(OnFirebaseChatDataChange listener) {
        mListeners.remove(listener);
    }

    public void attachNetworkDatabase() {
        if (mChatEventListener == null) {
            mChatEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Message> messages = new ArrayList<>();

                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        Message message = messageSnapshot.getValue(Message.class);
                        message.setKey(messageSnapshot.getKey());
                        messages.add(message);
                    }

                    for (OnFirebaseChatDataChange listener : mListeners) {
                        listener.notifyNewMessages(messages);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }
            };

            mChatDbReference.addValueEventListener(mChatEventListener);
        }
    }

    public void detachNetworkDatabase() {
        if (mChatEventListener != null) {
            mChatDbReference.removeEventListener(mChatEventListener);
        }
        mChatEventListener = null;
    }

    public void pushMessage(Message msg) {
        Log.d(TAG, "Push Message: " + msg);
        DatabaseReference newMessagePush = mChatDbReference.push();
        newMessagePush.setValue(msg);
    }

    public void updateMessage(Message msg) {
        Log.d(TAG, "Update Message: " + msg);

//        Map<String, Object> msgUpdate = new HashMap<>();
//        msgUpdate.put(msg.getKey() + "/bookmarked", msg.isBookmarked());
//
//        mChatDbReference.updateChildren(msgUpdate);
        mChatDbReference.child(msg.getKey()).child("bookmarked").setValue(msg.isBookmarked());
    }

}
