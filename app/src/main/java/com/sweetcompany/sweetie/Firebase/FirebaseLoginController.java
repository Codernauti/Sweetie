package com.sweetcompany.sweetie.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 27/05/2017.
 */

public class FirebaseLoginController{
    private static final String TAG = "FirebaseRegistrerController";
    private static final int RC_SIGN_IN = 9001;

    private final DatabaseReference mRegisterDbReference;
    private List<FirebaseLoginController.OnFirebaseLoginChecked> mListeners;
    private static FirebaseLoginController mInstance;

    private FirebaseLoginController() {
        mListeners = new ArrayList<>();
        this.mRegisterDbReference = FirebaseDatabase.getInstance()
                .getReference();
    }

    public interface OnFirebaseLoginChecked {
        void notifyUserChecked(UserFB user);
    }

    public static FirebaseLoginController getInstance() {
        if (mInstance == null) {
            mInstance = new FirebaseLoginController();
        }
        return mInstance;
    }

    public void addUserCheckListener(OnFirebaseLoginChecked listener) {mListeners.add(listener);}

    public void retrieveUserDataFromQuery(String key){
        mRegisterDbReference.child("users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFB user = dataSnapshot.getValue(UserFB.class);
                for (FirebaseLoginController.OnFirebaseLoginChecked listener : mListeners) {
                    listener.notifyUserChecked(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
