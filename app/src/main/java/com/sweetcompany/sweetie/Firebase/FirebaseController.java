package com.sweetcompany.sweetie.Firebase;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sweetcompany.sweetie.Actions.ActionFB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 17/05/2017.
 */

public class FirebaseController {

    interface OnFirebaseDataChange {
        void notifyDataChange(List<ActionFB> newData);
    }

    private static FirebaseController sInstance;

    //firebase
    private GoogleApiClient mGoogleApiClient;
    private static FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    private static FirebaseDatabase database;
    private static DatabaseReference mFirebaseReference;
    private static DatabaseReference mUserReference;
    private static DatabaseReference mActionsReference;

    private List<OnFirebaseDataChange> mListeners = new ArrayList<>();


    private FirebaseController(){
        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        //inizialize FirebaseDatabase
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true); // enable disk persistence
        mUserReference = database.getReference().child("users");
        mActionsReference = database.getReference().child("actions");
    }

    public static FirebaseController getInstance() {
        if (sInstance == null) {
            sInstance = new FirebaseController();
        }
        return sInstance;
    }

    public void addListener(OnFirebaseDataChange listener) {
        mListeners.add(listener);
    }

    public FirebaseUser getFirebaseUser() {
        return mFirebaseUser;
    }

    public FirebaseAuth getAuth() {
        return mFirebaseAuth;
    }

    public FirebaseDatabase getDatabase(){
        return database;
    }

    public DatabaseReference getDatabaseUserReferences(){
        return mUserReference;
    }

    public DatabaseReference getDatabaseActionsReferences(){
        return mActionsReference;
    }
}