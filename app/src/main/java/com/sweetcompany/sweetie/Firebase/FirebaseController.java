package com.sweetcompany.sweetie.Firebase;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.Actions.ActionFB;
import com.sweetcompany.sweetie.MainActivity;

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

    private Context mContext;
    private MainActivity mMainActivity;

    //firebase
    private GoogleApiClient mGoogleApiClient;
    private static FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    private static FirebaseDatabase database;
    private static DatabaseReference mFirebaseReference;
    private static DatabaseReference mUserReference;
    private static DatabaseReference mActionsReference;

    private List<OnFirebaseDataChange> mListeners = new ArrayList<>();


    public static FirebaseController getInstance() {
        if (sInstance == null) {
            sInstance = new FirebaseController();
        }
        return sInstance;
    }

    public static void init(){
        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        //inizialize FirebaseDatabase
        database = FirebaseDatabase.getInstance();
        mUserReference = database.getReference().child("users");
        mActionsReference = database.getReference().child("actions");
    }

    public void addListener(OnFirebaseDataChange listener) {
        mListeners.add(listener);
    }

    public void setContext(Context context) {
        mContext = context.getApplicationContext();
    }

    public Context getAppContext() {
        return mContext;
    }

    public MainActivity getRipActivity() {
        return mMainActivity;
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
        mFirebaseReference = database.getReference().child("actions");

        return mFirebaseReference;
    }

    public void attachDataChange() {
        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener actionsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                ActionFB action = dataSnapshot.getValue(ActionFB.class);

                for (OnFirebaseDataChange listener : mListeners) {
//                    listener.notifyDataChange(action);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase ;)", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mActionsReference.addValueEventListener(actionsListener);
    }
}
