package com.sweetcompany.sweetie.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.Utils.DataMaker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lucas on 30/05/2017.
 */

public class FirebasePairingController {
    private static final String TAG = "FirebasePairingControl";

    private List<OnFirebasePairingListener> mListeners = new ArrayList<>();
    private final String mUserId;

    private final DatabaseReference mUserPairingRequests;
    private final DatabaseReference mUsers;
    private final DatabaseReference mPairingRequests;
    private final DatabaseReference mCouples;

    private ValueEventListener mUserPairingRequestsListener;
    private ValueEventListener mUsersEqualToListener;

    public interface OnFirebasePairingListener {
        void onDownloadPairingRequestsCompleted(List<PairingRequestFB> userPairingRequests);
        void onSearchUserWithPhoneNumberFinished(UserFB user);
        void onCreateNewCoupleComplete();
        void onCreateNewPairingRequestComplete();
    }

    public FirebasePairingController(String userUid) {
        mUserId = userUid;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mUserPairingRequests = database.getReference().child(Constraints.PAIRING_REQUESTS_NODE).child(mUserId);
        mUsers = database.getReference().child(Constraints.USERS_NODE);
        mPairingRequests = database.getReference().child(Constraints.PAIRING_REQUESTS_NODE);
        mCouples = database.getReference().child(Constraints.COUPLES_NODE);

        mUserPairingRequests.keepSynced(true);
    }


    public void addListener(OnFirebasePairingListener listener) {mListeners.add(listener);}

    public void removeListener(OnFirebasePairingListener listener) {mListeners.remove(listener);}

    public void detachFromFirebase() {
        if (mUserPairingRequests != null) {
            mUserPairingRequests.removeEventListener(mUserPairingRequestsListener);
        }
        if (mUsersEqualToListener != null) {
            mUsers.removeEventListener(mUsersEqualToListener);
        }
    }


    public void downloadPairingRequest() {
        // N.B. we use a SingleEventListener because
        // if we attach a ValueEventListener when we remove the request this listener start a callback
        mUserPairingRequestsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.toString());

                List<PairingRequestFB> userPairingRequests = new ArrayList<>();
                for (DataSnapshot pairingRequestData : dataSnapshot.getChildren()) {
                    PairingRequestFB pairingRequest = pairingRequestData.getValue(PairingRequestFB.class);
                    pairingRequest.setKey(pairingRequestData.getKey());
                    userPairingRequests.add(pairingRequest);
                }

                for (OnFirebasePairingListener listener : mListeners) {
                    listener.onDownloadPairingRequestsCompleted(userPairingRequests);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        mUserPairingRequests.addListenerForSingleValueEvent(mUserPairingRequestsListener);
    }

    public void createNewCouple(String userUid, String partnerUid) {
        try {
            String now = DataMaker.get_UTC_DateTime();
            CoupleFB newCouple = new CoupleFB(userUid, partnerUid, now);
            mUserPairingRequests.child(partnerUid).removeValue();

            DatabaseReference newCoupleRef = mCouples.push();
            newCoupleRef.setValue(newCouple)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    for (OnFirebasePairingListener listener : mListeners) {
                        listener.onCreateNewCoupleComplete();
                    }
                }
            });

            // create a couple-info object
            CoupleInfoFB coupleInfo = new CoupleInfoFB(newCoupleRef.getKey(), now);

            // update "/users/<userUid>/couple-info/" and "/users/<partnerUid>/couple-info/"
            Map<String, Object> usersCoupleUpdates = new HashMap<>();
            usersCoupleUpdates.put(userUid + "/" + Constraints.COUPLE_INFO_NODE, coupleInfo);
            usersCoupleUpdates.put(partnerUid + "/" + Constraints.COUPLE_INFO_NODE, coupleInfo);

            mUsers.updateChildren(usersCoupleUpdates);
        }
        catch (ParseException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    public void searchUserWithPhoneNumber(final String phonePartner) {
        mUsersEqualToListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // only one user could have that phone number
                DataSnapshot data = dataSnapshot.getChildren().iterator().next();
                Log.d(TAG, data.toString());
                UserFB user = data.getValue(UserFB.class);
                user.setKey(data.getKey());

                /*for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.d(TAG, data.toString());
                    UserFB user = data.getValue(UserFB.class);
                    user.setKey(data.getKey());
                }*/

                for (OnFirebasePairingListener listener : mListeners) {
                    listener.onSearchUserWithPhoneNumberFinished(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        mUsers.orderByChild("phone").equalTo(phonePartner).addListenerForSingleValueEvent(mUsersEqualToListener);
    }

    public void createNewPairingRequest(UserFB user, String userPhoneNumber) {
        PairingRequestFB newRequest = new PairingRequestFB(userPhoneNumber);
        mPairingRequests.child(user.getKey())
                        .child(mUserId)
                        .setValue(newRequest)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                for (OnFirebasePairingListener listener : mListeners) {
                                    listener.onCreateNewPairingRequestComplete();
                                }
                            }
                        });
    }

}
