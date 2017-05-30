package com.sweetcompany.sweetie.Firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.Registration.UserVM;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lucas on 24/05/2017.
 */

public class FirebaseRegisterController {
    private static final String TAG = "FirebaseRegistrerController";

    private final DatabaseReference mRegisterDbReference;
    private List<OnFirebaseUserDataFound> mUserDataListeners;
    private static FirebaseRegisterController mInstance;


    private FirebaseRegisterController() {
        mUserDataListeners = new ArrayList<>();
        this.mRegisterDbReference = FirebaseDatabase.getInstance()
                .getReference();
    }

    public interface OnFirebaseUserDataFound {
        void notifyUserFound(SweetUser sweetUser);
    }

    public static FirebaseRegisterController getInstance() {
        if (mInstance == null) {
            mInstance = new FirebaseRegisterController();
        }
        return mInstance;
    }

    public void saveUserData(String token, UserVM user) {
        mRegisterDbReference.child("users").child(token).setValue(user);
    }


    public void addUserDataListener(OnFirebaseUserDataFound listener) {mUserDataListeners.add(listener);}



    //method for quering the database for getting data
    public void retrieveUserDataFromQuery(String orderBy, String endAt){
        mRegisterDbReference.child("users").orderByChild(orderBy).equalTo(endAt).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot=dataSnapshot.getChildren().iterator().next();
                SweetUser users = dataSnapshot.getValue(SweetUser.class);
                for (FirebaseRegisterController.OnFirebaseUserDataFound listener : mUserDataListeners) {
                    listener.notifyUserFound(users);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}