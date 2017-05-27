package com.sweetcompany.sweetie.Firebase;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;

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
        void notifyUserChecked(SweetUser user);
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
                SweetUser user = dataSnapshot.getValue(SweetUser.class);
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
