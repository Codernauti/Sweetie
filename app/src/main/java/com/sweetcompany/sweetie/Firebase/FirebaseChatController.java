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
 * Created by Eduard on 21-May-17.
 */

public class FirebaseChatController {

    private static final String TAG = "FirebaseChatController";

    private final DatabaseReference mChatDbReference;

    private static FirebaseChatController mInstance;
    private List<OnFirebaseChatDataChange> mListeners;
    private ValueEventListener mChatEventListener;


    public interface OnFirebaseChatDataChange {
        void notifyNewMessages(List<MessageFB> messages);
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
                    List<MessageFB> messageFBs = new ArrayList<>();

                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        MessageFB messageFB = messageSnapshot.getValue(MessageFB.class);
                        messageFB.setKey(messageSnapshot.getKey());
                        messageFBs.add(messageFB);
                    }

                    for (OnFirebaseChatDataChange listener : mListeners) {
                        listener.notifyNewMessages(messageFBs);
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

    public void pushMessage(MessageFB msg) {
        Log.d(TAG, "Push MessageFB: " + msg);
        DatabaseReference newMessagePush = mChatDbReference.push();
        newMessagePush.setValue(msg);
    }

    public void updateMessage(MessageFB msg) {
        Log.d(TAG, "Update MessageFB: " + msg);
        // TODO: update only bookmarked??
        mChatDbReference.child(msg.getKey()).child("bookmarked").setValue(msg.isBookmarked());
    }

}
