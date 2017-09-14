package com.sweetcompany.sweetie.firebase;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sweetcompany.sweetie.model.UserFB;


public class FirebaseRegisterController {
    private static final String TAG = "RegisterController";

    private final DatabaseReference mUsers;
    private Listener mListener;

    public interface Listener {
        void onUserUploaded();
    }

    public FirebaseRegisterController() {
        mUsers = FirebaseDatabase.getInstance().getReference(Constraints.USERS);
    }

    public void saveUserData(UserFB user) {
        // use auth user uid for id of firebase /users/<user_uid>/"user"
        mUsers.child(user.getKey())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (mListener != null) {
                            mListener.onUserUploaded();
                        }
                    }
                });
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }


    //method for quering the database for getting data
    /*public void retrieveUserDataFromQuery(String orderBy, String endAt){
        mUsers.orderByChild(orderBy).equalTo(endAt).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot=dataSnapshot.getChildren().iterator().next();
                String key = dataSnapshot.getKey();
                UserFB user = dataSnapshot.getValue(UserFB.class);
                user.setKey(key);
                for (Listener listener : mListeners) {
                    listener.onUserUploaded(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }*/
}