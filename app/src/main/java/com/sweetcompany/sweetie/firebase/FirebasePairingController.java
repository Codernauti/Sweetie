package com.sweetcompany.sweetie.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.utils.DataMaker;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.model.CoupleFB;
import com.sweetcompany.sweetie.model.PairingRequestFB;
import com.sweetcompany.sweetie.model.UserFB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lucas on 30/05/2017.
 */

public class FirebasePairingController {
    private static final String TAG = "FbPairingController";

    private List<PairingControllerListener> mListeners = new ArrayList<>();
    private NewPairingListener mActivityListener;

    private final String mUserId;
    private final String mUserUsername;

    private final DatabaseReference mUserPairingRequests;
    private final DatabaseReference mUsers;
    private final DatabaseReference mDatabase;

    private ValueEventListener mUserPairingRequestsListener;
    private ValueEventListener mUsersEqualToListener;

    public interface NewPairingListener {
        void onCreateNewPairingRequestComplete(String futurePartnerUid);
    }

    public interface PairingControllerListener {
        void onDownloadPairingRequestsCompleted(List<PairingRequestFB> userPairingRequests);
        void onSearchUserWithPhoneNumberFinished(UserFB user);
        void onCreateNewCoupleComplete();
        void onCreateNewPairingRequestComplete();
    }


    public FirebasePairingController(String userUid, String userUsername) {
        mUserId = userUid;
        mUserUsername = userUsername;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        mUserPairingRequests = database.getReference().child(Constraints.PAIRING_REQUESTS).child(mUserId);
        mUsers = database.getReference().child(Constraints.USERS);

        mUserPairingRequests.keepSynced(true);
    }


    public void addListener(PairingControllerListener listener) {mListeners.add(listener);}

    public void removeListener(PairingControllerListener listener) {mListeners.remove(listener);}

    public void setPairingListener(NewPairingListener listener) { mActivityListener = listener; }


    public void detachListeners() {
        if (mUserPairingRequestsListener != null) {
            mUserPairingRequests.removeEventListener(mUserPairingRequestsListener);
        }
        mUserPairingRequestsListener = null;

        if (mUsersEqualToListener != null) {
            mUsers.removeEventListener(mUsersEqualToListener);
        }
        mUsersEqualToListener = null;
    }


    public void downloadPairingRequest() {
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

                for (PairingControllerListener listener : mListeners) {
                    listener.onDownloadPairingRequestsCompleted(userPairingRequests);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        // N.B. we use a addForSingleValueEvent because
        // if we use addForValueEventListener when we remove the request this listener start a callback
        mUserPairingRequests.addListenerForSingleValueEvent(mUserPairingRequestsListener);
    }

    public void createNewCouple(PairingRequestFB partnerPairingRequest) {
        String now = DataMaker.get_UTC_DateTime();
        String partnerUid = partnerPairingRequest.getKey();
        String partnerUsername = partnerPairingRequest.getUsername();
        CoupleFB newCouple = new CoupleFB(mUserId, partnerUid, mUserUsername, partnerUsername, now);

        DatabaseReference newCoupleRef = mDatabase.child(Constraints.COUPLES).push();
        String newCoupleKey = newCoupleRef.getKey();

        Map<String, Object> updates = new HashMap<>();

        removeAcceptedPairingRequest(updates, partnerUid);

        addNewCouple(updates, newCouple, newCoupleKey);

        updateUsersCoupleInfo(updates, newCoupleKey, partnerUid);

        mDatabase.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                for (PairingControllerListener listener : mListeners) {
                    listener.onCreateNewCoupleComplete();
                }
            }
        });
    }

    private void removeAcceptedPairingRequest(Map<String, Object> updates, String partnerUid) {
        // remove the pairing request received
        // pairing-request/<mUserId>/<partnerUid>
        String userPairingRequestUrl = Constraints.PAIRING_REQUESTS + "/" +
                mUserId + "/" +
                partnerUid;
        updates.put(userPairingRequestUrl, null);
    }

    private void addNewCouple(Map<String, Object> updates, CoupleFB newCouple, String newCoupleKey) {
        // push the new couple: couples/<newCoupleUid>
        String newCoupleUrl = Constraints.COUPLES + "/" + newCoupleKey;
        updates.put(newCoupleUrl, newCouple);
    }

    private void updateUsersCoupleInfo(Map<String, Object> updates, String newCoupleKey, String partnerUid) {
        // users/<userUid>/coupleInfo
        String userActiveCoupleUrl = Constraints.USERS + "/" +
                mUserId + "/" +
                Constraints.COUPLE_INFO + "/" +
                Constraints.ACTIVE_COUPLE;
        updates.put(userActiveCoupleUrl, newCoupleKey);

        // users/<partnerUid>/coupleInfo
        String partnerActiveCoupleUrl = Constraints.USERS + "/" +
                partnerUid + "/" +
                Constraints.COUPLE_INFO + "/" +
                Constraints.ACTIVE_COUPLE;
        updates.put(partnerActiveCoupleUrl, newCoupleKey);
    }


    public void searchUserWithPhoneNumber(final String phonePartner) {
        if (mUsersEqualToListener == null) {
            mUsersEqualToListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // only one user could have that phone number
                    Iterator<DataSnapshot> resultIterator = dataSnapshot.getChildren().iterator();
                    if (resultIterator.hasNext()) {
                        DataSnapshot userDataSnapshot = resultIterator.next();
                        Log.d(TAG, userDataSnapshot.toString());

                        UserFB user = userDataSnapshot.getValue(UserFB.class);
                        user.setKey(userDataSnapshot.getKey());

                        for (PairingControllerListener listener : mListeners) {
                            listener.onSearchUserWithPhoneNumberFinished(user);
                        }
                    }
                    else {
                        Log.d(TAG, "No user with that phone number.");
                        for (PairingControllerListener listener : mListeners) {
                            listener.onSearchUserWithPhoneNumberFinished(null);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            };
            mUsers.orderByChild("phone").equalTo(phonePartner).addListenerForSingleValueEvent(mUsersEqualToListener);
        }
    }

    public void createNewPairingRequest(final UserFB futurePartner, String userUsername,
                                        String userPhoneNumber, String oldPairingRequestedUserUid) {
        PairingRequestFB newRequest = new PairingRequestFB(userUsername, userPhoneNumber);

        Map<String, Object> updates = new HashMap<>();

        if (!oldPairingRequestedUserUid.equals(SharedPrefKeys.DEFAULT_VALUE)) {
            // remove previous pairing request send by mUserId
            // pairing-request/<oldPairingRequestUserUid>/<mUserId>
            String oldUserPairingRequestUrl =   Constraints.PAIRING_REQUESTS + "/" +
                                                oldPairingRequestedUserUid + "/" +
                                                mUserId;
            updates.put(oldUserPairingRequestUrl, null);
        }

        // pairing-request/<futurePartner.getKey()>/<mUserId>
        String receiverPairingRequestsUrl =     Constraints.PAIRING_REQUESTS + "/" +
                                                futurePartner.getKey() + "/" +
                                                mUserId;
        updates.put(receiverPairingRequestsUrl, newRequest);

        // mUserId push a pairing-request to user.getKey
        // users/<mUserId>/futurePartner
        String userFuturePartnerUrl =   Constraints.USERS + "/" +
                                        mUserId + "/" +
                                        Constraints.FUTURE_PARTNER;
        updates.put(userFuturePartnerUrl, futurePartner.getKey());

        mDatabase.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mActivityListener.onCreateNewPairingRequestComplete(futurePartner.getKey());

                for (PairingControllerListener listener : mListeners) {
                    listener.onCreateNewPairingRequestComplete();
                }
            }
        });
    }

}
