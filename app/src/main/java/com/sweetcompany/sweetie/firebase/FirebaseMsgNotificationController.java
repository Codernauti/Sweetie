package com.sweetcompany.sweetie.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sweetcompany.sweetie.model.MsgNotification;

/**
 * Created by Eduard on 01-Sep-17.
 */

public class FirebaseMsgNotificationController {

    private final String mMsgNotificationRoomUrl;
    private final DatabaseReference mMsgNotificationRoomRef;

    private ChildEventListener mMsgNotificationRoomListener;

    private MsgNotificationControllerListener mListener;

    public interface MsgNotificationControllerListener {
        void onMessageArrived(MsgNotification msg);
    }


    public FirebaseMsgNotificationController(String userUid) {
        mMsgNotificationRoomUrl = Constraints.MSG_NOTIFICATION_ROOMS + "/" + userUid;

        mMsgNotificationRoomRef = FirebaseDatabase.getInstance().getReference(mMsgNotificationRoomUrl);
    }

    public void setListener(MsgNotificationControllerListener listener) { mListener = listener; }



    public void attachListener() {
        if (mMsgNotificationRoomListener == null) {
            mMsgNotificationRoomListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MsgNotification msg = dataSnapshot.getValue(MsgNotification.class);
                    msg.setUid(dataSnapshot.getKey());

                    if (mListener != null) {
                        mListener.onMessageArrived(msg);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mMsgNotificationRoomRef.addChildEventListener(mMsgNotificationRoomListener);
        }
    }

    public void detach() {
        if (mMsgNotificationRoomListener != null) {
            mMsgNotificationRoomRef.removeEventListener(mMsgNotificationRoomListener);
        }
        mMsgNotificationRoomListener = null;
    }

    public void removeMsgNotification(String msgUid) {
        mMsgNotificationRoomRef.child(msgUid).removeValue();
    }
}
