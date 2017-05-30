package com.sweetcompany.sweetie.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.Registration.PairingRequestVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 30/05/2017.
 */

public class FirebasePairingController {
    private static final String TAG = "FirebasePairingController";
    private ValueEventListener mPairingEventListener;
    private List<FirebasePairingController.OnFirebasePairingDataChange> mListeners;
    private final DatabaseReference mPairingDbReference;
    private static FirebasePairingController mInstance;

    private FirebasePairingController() {
        mListeners = new ArrayList<>();
        this.mPairingDbReference = FirebaseDatabase.getInstance()
                .getReference();
    }

    public static FirebasePairingController getInstance() {
        if (mInstance == null) {
            mInstance = new FirebasePairingController();
        }
        return mInstance;
    }

    public interface OnFirebasePairingDataChange {
        void notifyNewRequests(List<PairingRequest> pairingRequests);
    }


    public void attachNetworkDatabase() {
        if (mPairingEventListener == null) {
            mPairingEventListener = new ValueEventListener() {
                List<PairingRequest> pairingRequests = new ArrayList<>();
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                        PairingRequest pairingRequest = requestSnapshot.getValue(PairingRequest.class);
                        pairingRequest.setKey(requestSnapshot.getKey());
                        pairingRequests.add(pairingRequest);
                    }
                    for (FirebasePairingController.OnFirebasePairingDataChange listener : mListeners) {
                        listener.notifyNewRequests(pairingRequests);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mPairingDbReference.child("pairing-requests").addValueEventListener(mPairingEventListener);
        }
    }

    public void detachNetworkDatabase() {
        if (mPairingEventListener != null) {
            mPairingDbReference.removeEventListener(mPairingEventListener);
        }
        mPairingEventListener = null;
    }

    public void saveRequest(PairingRequestVM pairingRequest){
        mPairingDbReference.child("pairing-requests").push().setValue(new PairingRequest(pairingRequest));
    }

    public void addListener(OnFirebasePairingDataChange listener) {mListeners.add(listener);}

    public void removeListener(OnFirebasePairingDataChange listener) {mListeners.remove(listener);}

    public void deletePairingRequest(String keyPairingRequest){
        mPairingDbReference.child("pairing-requests").child(keyPairingRequest).removeValue();
    }
}
