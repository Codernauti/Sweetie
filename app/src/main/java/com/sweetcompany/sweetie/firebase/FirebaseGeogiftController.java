package com.sweetcompany.sweetie.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.sweetcompany.sweetie.model.MessageFB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ghiro on 07/08/2017.
 */

public class FirebaseGeogiftController {

    private static final String TAG = "FbGeogiftController";

    private final String mActionUid;

    private final String mGeogiftMessagesUrl;          // geogift-message/<couple_uid>/<geogift_uid>
    private final String mActionUrl;                // actions/<couple_uid>/<action_uid>

    private final DatabaseReference mDatabase;
    private final DatabaseReference mGeogift;
    private final DatabaseReference mGeogiftItems;
    private final DatabaseReference mAction;

    private final StorageReference mStorageRef;
    private final FirebaseStorage mStorage;

    private final String coupleID;

    private ValueEventListener mGeogiftListener;
    private ChildEventListener mGeogiftItemsListener;


    private List<FirebaseGeogiftController.GeogiftControllerListener> mListeners = new ArrayList<>();

    public interface GeogiftControllerListener {
        /*void onGeogiftChanged(GeogiftFB geogift);

        void onMessageAdded(MessageFB message);
        void onMessageRemoved(MessageFB message);
        void onMessageChanged(MessageFB message);
        void onUploadPercent(MessageFB media, int perc);*/
    }


    public FirebaseGeogiftController(String coupleUid, String geogiftKey, String actionKey) {
        mActionUid = actionKey;

        mGeogiftMessagesUrl = Constraints.GEOGIFT_ITEMS + "/" + coupleUid + "/" + geogiftKey;
        mActionUrl = Constraints.ACTIONS + "/" + coupleUid + "/" + actionKey;

        FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
        mDatabase = firebaseDb.getReference();

        mGeogift = firebaseDb.getReference(Constraints.GEOGIFT + "/" + coupleUid + "/" + geogiftKey);
        mGeogiftItems = firebaseDb.getReference(Constraints.GEOGIFT_ITEMS + "/" + coupleUid + "/" + geogiftKey);
        mAction = firebaseDb.getReference(Constraints.ACTIONS + "/" + coupleUid + "/" + actionKey);

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        coupleID = coupleUid;
    }

    public void addListener(FirebaseGeogiftController.GeogiftControllerListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(FirebaseGeogiftController.GeogiftControllerListener listener) {
        mListeners.remove(listener);
    }


    public void attachListeners() {
        /*if (mGeogiftItemsListener == null) {
            mGeogiftItemsListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MessageFB newMessage = dataSnapshot.getValue(MessageFB.class);
                    newMessage.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onMessageAdded to geogift: " + newMessage.getText());

                    for (FirebaseGeogiftController.GeogiftControllerListener listener : mListeners) {
                        //listener.onMessageAdded(newMessage);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    MessageFB newMessage = dataSnapshot.getValue(MessageFB.class);
                    newMessage.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onChildChanged of geogift: " + newMessage.getText());

                    for (FirebaseGeogiftController.GeogiftControllerListener listener : mListeners) {
                        //listener.onMessageChanged(newMessage);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    MessageFB removedMessage = dataSnapshot.getValue(MessageFB.class);
                    Log.d(TAG, "onMessageRemoved from geogift: " + removedMessage.getText());

                    for (FirebaseGeogiftController.GeogiftControllerListener listener : mListeners) {
                        //listener.onMessageRemoved(removedMessage);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            mGeogiftItems.addChildEventListener(mGeogiftItemsListener);
        }

        if (mGeogiftListener == null) {
            mGeogiftListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // TODO: test
                    GeogiftFB geogift = dataSnapshot.getValue(GeogiftFB.class);
                    geogift.setKey(dataSnapshot.getKey());

                    for (FirebaseGeogiftController.GeogiftControllerListener listener : mListeners) {
                        listener.onGeogiftChanged(geogift);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mGeogift.addValueEventListener(mGeogiftListener);
        }*/
    }

    public void detachListeners() {
        /*if (mGeogiftListener != null) {
            mGeogift.removeEventListener(mGeogiftListener);
        }
        mGeogiftListener = null;

        if (mGeogiftItemsListener != null) {
            mGeogiftItems.removeEventListener(mGeogiftItemsListener);
        }
        mGeogiftItemsListener = null;*/
    }
}
