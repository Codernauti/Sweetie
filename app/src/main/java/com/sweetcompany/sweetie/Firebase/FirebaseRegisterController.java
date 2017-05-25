package com.sweetcompany.sweetie.Firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.Registration.PairingRequestVM;
import com.sweetcompany.sweetie.Registration.RegisterPresenter;
import com.sweetcompany.sweetie.Registration.UserVM;
import com.sweetcompany.sweetie.Utils.Utility;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lucas on 24/05/2017.
 */

public class FirebaseRegisterController {
    private static final String TAG = "FirebaseRegistrerController";

    private final DatabaseReference mRegisterDbReference;
    private List<OnFirebaseRegisterDataChange> mListeners;
    private List<OnFirebaseUserDataFound> mUserDataListeners;
    private static FirebaseRegisterController mInstance;
    private ValueEventListener mRegisterEventListener;

    private FirebaseRegisterController() {
        mListeners = new ArrayList<>();
        mUserDataListeners = new ArrayList<>();
        this.mRegisterDbReference = FirebaseDatabase.getInstance()
                .getReference();
    }

    public interface OnFirebaseRegisterDataChange {
        void notifyNewRequests(List<PairingRequest> pairingRequests);
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

    public void saveRequest(PairingRequestVM pairingRequest){
        mRegisterDbReference.child("requests").push().setValue(new PairingRequest(pairingRequest));
    }

    public void addUserDataListener(OnFirebaseUserDataFound listener) {mUserDataListeners.add(listener);}

    public void removeUserDataListener(OnFirebaseUserDataFound listener) {mUserDataListeners.remove(listener);}

    public void addListener(OnFirebaseRegisterDataChange listener) {mListeners.add(listener);}

    public void removeListener(OnFirebaseRegisterDataChange listener) {mListeners.remove(listener);}

    public void attachNetworkDatabase() {
        if (mRegisterEventListener == null) {
            mRegisterEventListener = new ValueEventListener() {
                List<PairingRequest> pairingRequests = new ArrayList<>();
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                        PairingRequest pairingRequest = requestSnapshot.getValue(PairingRequest.class);
                        pairingRequest.setKey(requestSnapshot.getKey());
                        pairingRequests.add(pairingRequest);
                    }
                    for (FirebaseRegisterController.OnFirebaseRegisterDataChange listener : mListeners) {
                        listener.notifyNewRequests(pairingRequests);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mRegisterDbReference.child("requests").addValueEventListener(mRegisterEventListener);
        }
    }

    public void detachNetworkDatabase() {
        if (mRegisterEventListener != null) {
            mRegisterDbReference.removeEventListener(mRegisterEventListener);
        }
        mRegisterEventListener = null;
    }

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

    public void deletePairingRequest(String keyPairingRequest){
        mRegisterDbReference.child("requests").child(keyPairingRequest).removeValue();
    }
}