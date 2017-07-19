package com.sweetcompany.sweetie.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.UserFB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 27/05/2017.
 */

public class FirebaseLoginController{
    private static final String TAG = "FbRegistrerController";

    private final DatabaseReference mRegisterDbReference;
    private List<FbLoginControllerListener> mListeners = new ArrayList<>();;

    public interface FbLoginControllerListener {
        void onUserDownloadFinished(UserFB user);
    }

    public FirebaseLoginController() {
        mRegisterDbReference = FirebaseDatabase.getInstance().getReference();
    }


    public void addListener(FbLoginControllerListener listener) { mListeners.add(listener); }

    public void removeListener(FbLoginControllerListener listener) { mListeners.remove(listener); }

    public void retrieveUserDataFromQuery(String key){
        mRegisterDbReference.child("users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFB user = dataSnapshot.getValue(UserFB.class);
                for (FbLoginControllerListener listener : mListeners) {
                    listener.onUserDownloadFinished(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
