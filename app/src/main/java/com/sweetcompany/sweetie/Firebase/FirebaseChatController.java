package com.sweetcompany.sweetie.Firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
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

    private static FirebaseChatController mInstance;
    private List<OnFirebaseChatDataChange> mListeners;

    private final DatabaseReference mChatMessagesReference;

    // TODO: FirebaseChatController is specific for a single Chat
    // uri: /chat-messages/id_chat
    private DatabaseReference mSingleChatMessagesReference;
    private ChildEventListener mSingleChatMessagesListener;

    // TODO: choice between Chats or SingleChat (*)
    // uri: /chats
    private final DatabaseReference mChatsDbReference;
    private ValueEventListener mChatsEventListener;

    // TODO: (*)
    // uri: /chats/id_chat
    private DatabaseReference mSingleChatReference;
    private ValueEventListener mSingleChatListener;
    private DatabaseReference mActionReference;


    public interface OnFirebaseChatDataChange {
        void notifyChats(List<ChatFB> chats);

        void notifyNewMessage(MessageFB message);
        void notifyRemovedMessage(MessageFB message);
        void notifyChangedMessage(MessageFB message);
    }


    private FirebaseChatController() {
        mListeners = new ArrayList<>();
        mChatsDbReference = FirebaseDatabase.getInstance()
                                            .getReference().child("chats");
        mChatMessagesReference = FirebaseDatabase.getInstance()
                .getReference().child("chat-messages");
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

    public void attachNetworkDatabase(String chatKey) {
        // Get the reference for messages of a single chat
        mSingleChatMessagesReference = mChatMessagesReference.child(chatKey);

        // Attach a new Listener to this chat reference
        if (mSingleChatMessagesListener == null) {
            mSingleChatMessagesListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MessageFB newMessage = dataSnapshot.getValue(MessageFB.class);
                    newMessage.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onMessageAdded to chat: " + newMessage.getText());

                    for (OnFirebaseChatDataChange listener : mListeners) {
                        listener.notifyNewMessage(newMessage);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    MessageFB newMessage = dataSnapshot.getValue(MessageFB.class);
                    newMessage.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onChildChanged of chat: " + newMessage.getText());

                    for (OnFirebaseChatDataChange listener : mListeners) {
                        listener.notifyChangedMessage(newMessage);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    MessageFB removedMessage = dataSnapshot.getValue(MessageFB.class);
                    Log.d(TAG, "onMessageRemoved from chat: " + removedMessage.getText());

                    for (OnFirebaseChatDataChange listener : mListeners) {
                        listener.notifyRemovedMessage(removedMessage);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mSingleChatMessagesReference.addChildEventListener(mSingleChatMessagesListener);
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
        if (mSingleChatMessagesListener != null) {
            mSingleChatMessagesReference.removeEventListener(mSingleChatMessagesListener);
        }
        if (mChatsEventListener != null) {
            mChatsDbReference.removeEventListener(mChatsEventListener);
        }
        // Put to null for GC, because they have outer ref to this singleton class
        mSingleChatMessagesListener = null;
        mChatsEventListener = null;
    }

    public void updateMessage(MessageFB msg) {
        Log.d(TAG, "Update MessageFB: " + msg);
        // TODO: update only bookmarked??
        DatabaseReference ref = mSingleChatMessagesReference.child(msg.getKey()).child("bookmarked");
        ref.setValue(msg.isBookmarked());
    }

    public void updateAction(String actionKey, String description, String date){
        DatabaseReference mActionDescriptionReference = FirebaseDatabase.getInstance()
                .getReference().child("actions").child(actionKey).child("description");
        DatabaseReference mActionDataTimeReference = FirebaseDatabase.getInstance()
                .getReference().child("actions").child(actionKey).child("dataTime");
        mActionDescriptionReference.setValue(description);
        mActionDataTimeReference.setValue(date);
        //TODO add last user sender (wait until couple is done)
    }

    // TEST
    public void sendMessage(MessageFB msg, String actionKey) {
        if (mSingleChatMessagesReference != null) {
            Log.d(TAG, "Send MessageFB: " + msg);
            mSingleChatMessagesReference.push().setValue(msg);
            //TODO Best Practices : call methods inside or earlier?
            updateAction(actionKey, msg.getText(), msg.getDateTime());
        }
        else {
            Log.w(TAG, "sendMessage(): chat reference doesn't instantiate. Impossible to send message.");
        }
    }

}
