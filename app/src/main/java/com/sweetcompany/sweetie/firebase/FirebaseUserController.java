package com.sweetcompany.sweetie.firebase;

import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.UserFB;


public class FirebaseUserController {

    private static final String TAG = "UserController";

    private final DatabaseReference mUserRef;
    private ValueEventListener mUserRefListener;

    private UserControllerListener mListener;

    public interface UserControllerListener {
        void onUserChange(@Nullable UserFB newUserData);
    }

    public FirebaseUserController(String userUid, UserControllerListener listener) {
        mUserRef = FirebaseDatabase.getInstance().getReference(Constraints.USERS).child(userUid);
        mListener = listener;
    }

    public void attachListener() {
        if (mUserRefListener == null) {
            mUserRefListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserFB newUserData = dataSnapshot.getValue(UserFB.class);
                    newUserData.setKey(dataSnapshot.getKey());

                    if (mListener != null) {
                        mListener.onUserChange(newUserData);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mUserRef.addValueEventListener(mUserRefListener);
        }
    }

    public void detachListener() {
        if (mUserRefListener != null) {
            mUserRef.removeEventListener(mUserRefListener);
        }
        mUserRefListener = null;
    }
}
