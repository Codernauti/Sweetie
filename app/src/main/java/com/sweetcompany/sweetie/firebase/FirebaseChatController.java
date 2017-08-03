package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.ActionDiaryFB;
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.model.ChatFB;
import com.sweetcompany.sweetie.model.MessageFB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eduard on 21-May-17.
 */

public class FirebaseChatController {

    private static final String TAG = "FbChatController";

    private final String mActionUid;

    private final String mChatUrl;              // chat-message/<couple_uid>/<chat_uid>
    private final String mCoupleCalendarUrl;    // calendar/<couple_uid>
    private final String mCoupleActionsDiaryUrl;      // actionsDiary/<couple_uid>/<action_uid>

    private final DatabaseReference mDatabase;
    private final DatabaseReference mChat;
    private final DatabaseReference mChatMessages;
    private final DatabaseReference mAction;

    private ValueEventListener mChatListener;
    private ChildEventListener mChatMessagesListener;


    private List<ChatControllerListener> mListeners = new ArrayList<>();

    public interface ChatControllerListener {
        void onChatChanged(ChatFB chat);

        void onMessageAdded(MessageFB message);
        void onMessageRemoved(MessageFB message);
        void onMessageChanged(MessageFB message);
    }


    public FirebaseChatController(String coupleUid, String chatKey, String actionKey) {
        mActionUid = actionKey;

        mChatUrl = Constraints.CHAT_MESSAGES + "/" + coupleUid + "/" + chatKey;
        mCoupleCalendarUrl = Constraints.CALENDAR + "/" + coupleUid;
        mCoupleActionsDiaryUrl = "actionsDiary" + "/" + coupleUid;

        FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
        mDatabase = firebaseDb.getReference();

        mChat = firebaseDb.getReference(Constraints.CHATS + "/" + coupleUid + "/" + chatKey);
        mChatMessages = firebaseDb.getReference(Constraints.CHAT_MESSAGES + "/" + coupleUid + "/" + chatKey);
        mAction = firebaseDb.getReference(Constraints.ACTIONS + "/" + coupleUid + "/" + actionKey);
    }

    public void addListener(ChatControllerListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(ChatControllerListener listener) {
        mListeners.remove(listener);
    }


    public void attachListeners() {
        if (mChatMessagesListener == null) {
            mChatMessagesListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MessageFB newMessage = dataSnapshot.getValue(MessageFB.class);
                    newMessage.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onMessageAdded to chat: " + newMessage.getText());

                    for (ChatControllerListener listener : mListeners) {
                        listener.onMessageAdded(newMessage);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    MessageFB newMessage = dataSnapshot.getValue(MessageFB.class);
                    newMessage.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onChildChanged of chat: " + newMessage.getText());

                    for (ChatControllerListener listener : mListeners) {
                        listener.onMessageChanged(newMessage);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    MessageFB removedMessage = dataSnapshot.getValue(MessageFB.class);
                    Log.d(TAG, "onMessageRemoved from chat: " + removedMessage.getText());

                    for (ChatControllerListener listener : mListeners) {
                        listener.onMessageRemoved(removedMessage);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            mChatMessages.addChildEventListener(mChatMessagesListener);
        }

        if (mChatListener == null) {
            mChatListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // TODO: test
                    ChatFB chat = dataSnapshot.getValue(ChatFB.class);
                    chat.setKey(dataSnapshot.getKey());

                    for (ChatControllerListener listener : mListeners) {
                        listener.onChatChanged(chat);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mChat.addValueEventListener(mChatListener);
        }
    }

    public void detachListeners() {
        if (mChatListener != null) {
            mChat.removeEventListener(mChatListener);
        }
        mChatListener = null;

        if (mChatMessagesListener != null) {
            mChatMessages.removeEventListener(mChatMessagesListener);
        }
        mChatMessagesListener = null;
    }


    public void updateMessage(MessageFB msg) {
        Log.d(TAG, "Update MessageFB: " + msg);

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(mChatUrl + "/" + msg.getKey() + "/" + Constraints.BOOKMARK, msg.isBookmarked());

        String actionDiaryDataUrl = mCoupleActionsDiaryUrl + "/" + msg.getDate() + "/" + mActionUid;
        final String actionDiaryUrl = mCoupleCalendarUrl + "/" + msg.getYearAndMonth() + "/"
                                + msg.getDay() + "/" + mActionUid;

        if (msg.isBookmarked()) {
            ActionDiaryFB action = new ActionDiaryFB(ActionFB.CHAT, msg.getDate());

            updates.put(actionDiaryUrl, action);
            updates.put(actionDiaryDataUrl + "/" + msg.getKey(), msg);
        }
        else {
            updates.put(actionDiaryDataUrl + "/" + msg.getKey(), null);

            mDatabase.child(actionDiaryDataUrl)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() <= 0) {
                                // user remove all messages associated with this ActionDiary
                                mDatabase.child(actionDiaryUrl).removeValue();
                            }
                        }
                        public void onCancelled(DatabaseError databaseError) { }
            });
        }
        mDatabase.updateChildren(updates);
    }

    // push message to db and update action of this chat
    public void sendMessage(MessageFB msg) {
        Log.d(TAG, "Send MessageFB: " + msg);
        // TODO: use atomic operation with hashmap

        // push a message into mChatMessages reference
        mChatMessages.push().setValue(msg);

        // update description and dataTime of action of this associated Chat
        Map<String, Object> actionUpdates = new HashMap<>();
        actionUpdates.put("description", msg.getText());
        actionUpdates.put("dataTime", msg.getDateTime());
        mAction.updateChildren(actionUpdates);
    }

}
