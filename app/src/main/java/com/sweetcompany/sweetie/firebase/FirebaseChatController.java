package com.sweetcompany.sweetie.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sweetcompany.sweetie.model.ActionDiaryFB;
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.model.ChatFB;
import com.sweetcompany.sweetie.model.MessageFB;
import com.sweetcompany.sweetie.model.MsgNotification;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eduard on 21-May-17.
 */

public class FirebaseChatController extends FirebaseGeneralActionController {

    private static final String TAG = "FbChatController";

    private final String mChatUid;
    private final String mCoupleUid;

    // for actionDiary
    private String mChatTitle;
    private String mChatImageUri;

    // database
    private final String mChatMessagesUrl;                  // chat-message/<couple_uid>/<chat_uid>
    private final String mCoupleCalendarUrl;                // calendar/<couple_uid>
    private final String mCoupleActionsDiaryUrl;            // actionsDiary/<couple_uid>/<action_uid>
    private final String mActionUrl;                        // actions/<couple_uid>/<action_uid>
    private final String mNotificationRoomUrl;              // msg-notification-rooms/<user_uid>

    private final DatabaseReference mDatabaseRef;
    private final DatabaseReference mChatRef;
    private final DatabaseReference mChatMessagesRef;

    private final StorageReference mGalleryPhotoRef;

    private ValueEventListener mChatListener;
    private ChildEventListener mChatMessagesListener;

    // Start object for notification new message to partner
    private final MsgNotification mMsgDefaultNotification;


    private Listener mListener;

    public interface Listener {
        void onChatChanged(ChatFB chat);

        void onMessageAdded(MessageFB message);
        void onMessageRemoved(MessageFB message);
        void onMessageChanged(MessageFB message);
        void onUploadPercent(MessageFB media, int perc);
    }


    public FirebaseChatController(String coupleUid, String chatKey, String chatTitle, String actionUid,
                                  String userUid, String partnerUid) {
        super(coupleUid, userUid, partnerUid, actionUid);

        mCoupleUid = coupleUid;
        mChatUid = chatKey;

        // first init
        mChatTitle = chatTitle;
        //mChatImageUri = imageUri;

        mMsgDefaultNotification = new MsgNotification(chatKey, actionUid, chatTitle);

        mChatMessagesUrl = Constraints.CHAT_MESSAGES + "/" + coupleUid + "/" + chatKey;
        mCoupleCalendarUrl = Constraints.CALENDAR + "/" + coupleUid;
        mCoupleActionsDiaryUrl = Constraints.ACTIONS_DIARY + "/" + coupleUid;
        mActionUrl = Constraints.ACTIONS + "/" + coupleUid + "/" + actionUid;
        mNotificationRoomUrl = Constraints.MSG_NOTIFICATION_ROOMS + "/" + partnerUid;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mChatRef = mDatabaseRef.child(Constraints.CHATS + "/" + coupleUid + "/" + chatKey);
        mChatMessagesRef = mDatabaseRef.child(mChatMessagesUrl);

        String mGalleryPhotoUrl = Constraints.GALLERY_PHOTOS_DIRECTORY + "/" + mCoupleUid;
        mGalleryPhotoRef = FirebaseStorage.getInstance().getReference(mGalleryPhotoUrl);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }


    public void attachListeners() {
        super.attachListeners();

        if (mChatMessagesListener == null) {
            mChatMessagesListener = new ChildEventListener() {
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
                    MessageFB newMessage = dataSnapshot.getValue(MessageFB.class);
                    newMessage.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onChildChanged of chat: " + newMessage.getText());

                    if (mListener != null) {
                        mListener.onMessageChanged(newMessage);
                    }
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
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            mChatMessagesRef.addChildEventListener(mChatMessagesListener);
        }

        if (mChatListener == null) {
            mChatListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ChatFB chat = dataSnapshot.getValue(ChatFB.class);
                    chat.setKey(dataSnapshot.getKey());

                    mChatTitle = chat.getTitle();
                    //TODO ??
                    // mChatImageUri = chat.getImageUri(); was
                    mChatImageUri = chat.getUriCover();

                    if (mListener != null) {
                        mListener.onChatChanged(chat);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mChatRef.addValueEventListener(mChatListener);
        }
    }

    public void detachListeners() {
        super.detachListeners();

        if (mChatListener != null) {
            mChatRef.removeEventListener(mChatListener);
        }
        mChatListener = null;

        if (mChatMessagesListener != null) {
            mChatMessagesRef.removeEventListener(mChatMessagesListener);
        }
        mChatMessagesListener = null;
    }


    public void updateMessage(MessageFB msg) {
        Log.d(TAG, "Update MessageFB: " + msg);

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(mChatMessagesUrl + "/" + msg.getKey() + "/" + Constraints.BOOKMARK, msg.isBookmarked());

        String actionDiaryDataUrl = mCoupleActionsDiaryUrl + "/" + msg.getDate() + "/" + mChatUid;
        final String actionDiaryUrl = mCoupleCalendarUrl + "/" + msg.getYearAndMonth() + "/"
                                + msg.getDay() + "/" + mChatUid;

        if (msg.isBookmarked()) {
            ActionDiaryFB action = new ActionDiaryFB(ActionFB.CHAT, msg.getDate(), mChatTitle,
                    msg.getText(), mChatImageUri);

            updates.put(actionDiaryUrl, action);
            updates.put(actionDiaryDataUrl + "/" + msg.getKey(), msg);
        }
        else {
            updates.put(actionDiaryDataUrl + "/" + msg.getKey(), null);

            mDatabaseRef.child(actionDiaryDataUrl)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() <= 0) {
                                // user remove all messages associated with this ActionDiary
                                mDatabaseRef.child(actionDiaryUrl).removeValue();
                            }
                        }
                        public void onCancelled(DatabaseError databaseError) { }
            });
        }
        mDatabaseRef.updateChildren(updates);
    }


    public String sendMessage(MessageFB msg) {
        final String newMessageUid = mChatMessagesRef.push().getKey();
        addMessage(newMessageUid, msg);

        return newMessageUid;
    }

    private void addMessage(String msgUid, MessageFB msg) {
        Log.d(TAG, "Send MessageFB: " + msg + " of type: " + msg.getType());

        Map<String, Object> updates = new HashMap<>();

        // push a message into mChatMessagesRef reference
        updates.put(mChatMessagesUrl + "/" + msgUid, msg);

        // update description, dataTime and partnerNotificCounter of action of this associated Chat
        updates.put(mActionUrl + "/" + Constraints.Actions.DESCRIPTION, msg.getText());
        updates.put(mActionUrl + "/" + Constraints.Actions.DATE_TIME, msg.getDateTime());

        super.updateNotificationCounter(updates);

        // update msg-notification-room
        mMsgDefaultNotification.setText(msg.getText());
        updates.put(mNotificationRoomUrl + "/" + msgUid, mMsgDefaultNotification);

        mDatabaseRef.updateChildren(updates);
    }

    public String sendMedia(final MessageFB mediaMessage) {
        final String newMessageUid = mChatMessagesRef.push().getKey();

        Uri uriLocal = Uri.parse(mediaMessage.getUriLocal());
        StorageReference photoRef = mGalleryPhotoRef.child(uriLocal.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(uriLocal);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "onFailure sendFileFirebase " + exception.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            mediaMessage.setUriStorage(downloadUrl.toString());

                            addMessage(newMessageUid, mediaMessage);
                    }
                })
                .addOnProgressListener(
                new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        if (mListener != null) {
                            mListener.onUploadPercent(mediaMessage, ((int) progress));
                        }
                    }
                });

        return newMessageUid;
    }

}
