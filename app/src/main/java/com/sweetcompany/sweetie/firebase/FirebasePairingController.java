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
import com.sweetcompany.sweetie.Utils.DataMaker;
import com.sweetcompany.sweetie.Utils.Utility;

import java.text.ParseException;
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

    private final DatabaseReference mUserPairingRequests;
    private final DatabaseReference mUsers;
    private final DatabaseReference mPairingRequests;
    private final DatabaseReference mCouples;

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


    public FirebasePairingController(String userUid) {
        mUserId = userUid;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mUserPairingRequests = database.getReference().child(Constraints.PAIRING_REQUESTS_NODE).child(mUserId);
        mUsers = database.getReference().child(Constraints.USERS_NODE);
        mPairingRequests = database.getReference().child(Constraints.PAIRING_REQUESTS_NODE);
        mCouples = database.getReference().child(Constraints.COUPLES_NODE);

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

    public void createNewCouple(String partnerUid) {
        try {
            String now = DataMaker.get_UTC_DateTime();
            CoupleFB newCouple = new CoupleFB(mUserId, partnerUid, now);
            mUserPairingRequests.child(partnerUid).removeValue();

            DatabaseReference newCoupleRef = mCouples.push();
            newCoupleRef.setValue(newCouple)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    for (PairingControllerListener listener : mListeners) {
                        listener.onCreateNewCoupleComplete();
                    }
                }
            });

            // create a couple-info object
            CoupleInfoFB coupleInfo = new CoupleInfoFB(newCoupleRef.getKey(), now);

            // update "/users/<userUid>/couple-info/" and "/users/<partnerUid>/couple-info/"
            Map<String, Object> usersCoupleUpdates = new HashMap<>();
            usersCoupleUpdates.put(mUserId + "/" + Constraints.COUPLE_INFO_NODE, coupleInfo);
            usersCoupleUpdates.put(partnerUid + "/" + Constraints.COUPLE_INFO_NODE, coupleInfo);

            mUsers.updateChildren(usersCoupleUpdates);
        }
        catch (ParseException ex) {
            Log.d(TAG, ex.getMessage());
        }
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
                        // TODO: tell to the user no phone number found, send an invite to Sweetie
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            };
            mUsers.orderByChild("phone").equalTo(phonePartner).addListenerForSingleValueEvent(mUsersEqualToListener);
        }
    }

    public void createNewPairingRequest(UserFB futurePartner, String userPhoneNumber, String oldPairingRequestedUserUid) {
        PairingRequestFB newRequest = new PairingRequestFB(userPhoneNumber);

        if (!oldPairingRequestedUserUid.equals(Utility.DEFAULT_VALUE)) {
            // remove previous pairing request send by mUserId
            // "pairing-request/<oldPairingRequestUserUid>/<mUserId>/"
            mPairingRequests.child(oldPairingRequestedUserUid).child(mUserId).removeValue();
        }

        // TODO: save pairing request into shared preferences
        mActivityListener.onCreateNewPairingRequestComplete(futurePartner.getKey());


        // mUserId push a pairing-request to user.getKey
        // "pairing-request/<futurePartner.getKey()>/<mUserId>/<newRequest>"
        mPairingRequests.child(futurePartner.getKey())
                        .child(mUserId)
                        .setValue(newRequest)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                for (PairingControllerListener listener : mListeners) {
                                    listener.onCreateNewPairingRequestComplete();
                                }
                            }
                        });

        // save new pairing request sent by mUserId
        mUsers.child(mUserId + "/futurePartner").setValue(futurePartner.getKey());
    }

}