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

    private final DatabaseReference mMessagesDbReference;
    private final DatabaseReference mChatsDbReference;

    private static FirebaseChatController mInstance;
    private List<OnFirebaseChatDataChange> mListeners;

    private ValueEventListener mMessagesEventListener;
    private ValueEventListener mChatsEventListener;

    public interface OnFirebaseChatDataChange {
        void notifyNewMessages(List<MessageFB> messages);
        void notifyChats(List<ChatFB> chats);
    }


    private FirebaseChatController() {
        mListeners = new ArrayList<>();
        mMessagesDbReference = FirebaseDatabase.getInstance()
                                            .getReference().child("messages");
        mChatsDbReference = FirebaseDatabase.getInstance()
                                            .getReference().child("chats");
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
        if (mMessagesEventListener == null) {
            mMessagesEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<MessageFB> messages = new ArrayList<>();

                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        MessageFB messageFB = messageSnapshot.getValue(MessageFB.class);
                        messageFB.setKey(messageSnapshot.getKey());
                        messages.add(messageFB);
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

            mMessagesDbReference.addValueEventListener(mMessagesEventListener);
        }

        if (mChatsEventListener == null) {
            mChatsEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<ChatFB> chats = new ArrayList<>();

                    for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                        ChatFB chat = chatSnapshot.getValue(ChatFB.class);
                        chat.setKey(chatSnapshot.getKey());
                        chats.add(chat);
                    }

                    for (OnFirebaseChatDataChange listener : mListeners) {
                        listener.notifyChats(chats);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mChatsDbReference.addValueEventListener(mChatsEventListener);
        }
    }

    public void detachNetworkDatabase() {
        if (mMessagesEventListener != null) {
            mMessagesDbReference.removeEventListener(mMessagesEventListener);
        }
        if (mChatsEventListener != null) {
            mChatsDbReference.removeEventListener(mChatsEventListener);
        }
        // TODO why put at null?
        mMessagesEventListener = null;
        mChatsEventListener = null;
    }

    public void pushMessage(MessageFB msg) {
        Log.d(TAG, "Push MessageFB: " + msg);
        DatabaseReference newMessagePush = mMessagesDbReference.push();
        newMessagePush.setValue(msg);
    }

    public void updateMessage(MessageFB msg) {
        Log.d(TAG, "Update MessageFB: " + msg);
        // TODO: update only bookmarked??
        mMessagesDbReference.child(msg.getKey()).child("bookmarked").setValue(msg.isBookmarked());
    }

}
