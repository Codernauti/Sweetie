package com.sweetcompany.sweetie.Firebase;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lucas on 24/05/2017.
 */

public class FirebaseRegisterController {
    private static final String TAG = "FbRegistrerController";

    private final DatabaseReference mUsers;
    private List<FbRegisterControllerListener> mListeners = new ArrayList<>();

    public interface FbRegisterControllerListener {
        void onUserPushed();
    }

    public FirebaseRegisterController() {
        mUsers = FirebaseDatabase.getInstance().getReference(Constraints.USERS_NODE);
    }

    public void saveUserData(UserFB user) {
        // use auth user uid for id of firebase /users/<user_uid>/"user"
        mUsers.child(user.getKey())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        for(FbRegisterControllerListener listener : mListeners) {
                            listener.onUserPushed();
                        }
                    }
                });
    }

    public void addListener(FbRegisterControllerListener listener) {
        mListeners.add(listener);
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
                for (FbRegisterControllerListener listener : mListeners) {
                    listener.onUserPushed(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }*/
}