package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.MessageFB;

import java.util.HashMap;


public class FirebaseChatDiaryController {

    private static final String TAG = "ChatDiaryController";

    private final String mActionDiaryCalendarUrl;       // calendar/<couple_uid>/<YYYY-mm>/<dd>/<actionDiary_uid>
    private final String mActionDiaryUrl;               // actionsDiary/<couple_uid>/<YYY-mm-dd>/<actionDiary_uid>
    private final String mChatMessagesUrl;              // chat_messages/<couple_uid>/<actionDiary_uid>

    private final DatabaseReference mDatabaseRef;
    private final DatabaseReference mActionDiary;

    private ChildEventListener mActionDiaryListener;

    private Listener mListener;

    public interface Listener {
        void onMessageAdded(MessageFB message);
        void onMessageRemoved(MessageFB message);
    }

    public FirebaseChatDiaryController(String coupleUid, String actionDiaryDate, String actionDiaryUid /* chatUid */) {

        mActionDiaryCalendarUrl = Constraints.CALENDAR + "/"
                + coupleUid + "/"
                + actionDiaryDate.substring(0, 7) + "/"
                + actionDiaryDate.substring(8, 10) + "/"
                + actionDiaryUid;

        mActionDiaryUrl = Constraints.ACTIONS_DIARY + "/"
                + coupleUid + "/"
                + actionDiaryDate + "/"
                + actionDiaryUid;

        mChatMessagesUrl = Constraints.CHAT_MESSAGES + "/"
                + coupleUid + "/"
                + actionDiaryUid;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mActionDiary = mDatabaseRef.child(mActionDiaryUrl);
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
                    removedMessage.setKey(dataSnapshot.getKey());

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


    public void removeBookmarkedMessage(String messageUid) {
        HashMap<String, Object> updates = new HashMap<>();

        // Remove item from actionDiary
        updates.put(mActionDiaryUrl + "/" + messageUid, null);

        // Remove bookmark of item
        updates.put(mChatMessagesUrl + "/"
                + messageUid + "/"
                + Constraints.ChatMessages.BOOKMARK,
                false);

        mDatabaseRef.child(mActionDiaryUrl)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() <= 0) {
                            // user remove all messages associated with this ActionDiary
                            mDatabaseRef.child(mActionDiaryCalendarUrl).removeValue();
                        }
                    }
                    public void onCancelled(DatabaseError databaseError) { }
                });

        mDatabaseRef.updateChildren(updates);

    }

}
