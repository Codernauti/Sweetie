package com.sweetcompany.sweetie.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sweetcompany.sweetie.model.CoupleFB;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eduard on 20-Jul-17.
 */

public class FirebaseCoupleDetailsController {

    private static final String TAG = "CoupleDetailsController";

    private static final String ACTIVE_COUPLE_PARTIAL_URL = Constraints.COUPLE_INFO + "/" + Constraints.ACTIVE_COUPLE;
    private static final String ARCHIVED_COUPLES_PARTIAL_URL = Constraints.COUPLE_INFO + "/" + Constraints.ARCHIVED_COUPLES;

    private final DatabaseReference mDatabase;
    private final StorageReference mCoupleStorage;

    private final DatabaseReference mCouple;
    private ValueEventListener mCoupleListener;

    private final String mUserFuturePartnerUrl;         // users/<userUid>/futurePartner
    private final String mPartnerFuturePartnerUrl;      // users/<partnerUid>/futurePartner
    private final String mUserArchivedCouplesUrl;       // users/<userUid>/coupleInfo/activeCouple/<coupleUid>
    private final String mUserActiveCoupleUrl;          // users/<userUid>/coupleInfo/archivedCouples
    private final String mPartnerArchivedCouplesUrl;    // users/<partnerUid>/coupleInfo/activeCouple/<coupleUid>
    private final String mPartnerActiveCoupleUrl;       // users/<partnerUid>/coupleInfo/archivedCouples
    private final String mCoupleUidUrl;                 // couples/<coupleUid>

    private CoupleDetailsControllerListener mListener;

    public interface CoupleDetailsControllerListener {
        void onCoupleDetailsChanged(CoupleFB couple);
        void onImageUploadProgress(int progress);
    }


    public FirebaseCoupleDetailsController(String userUid, String partnerUid, String coupleUid) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCoupleStorage = FirebaseStorage.getInstance().getReference(Constraints.COUPLES_DETAILS + "/" + coupleUid);

        mUserFuturePartnerUrl = Constraints.USERS + "/" + userUid + "/" + Constraints.FUTURE_PARTNER;
        mPartnerFuturePartnerUrl = Constraints.USERS + "/" + partnerUid + "/" + Constraints.FUTURE_PARTNER;

        mUserArchivedCouplesUrl = buildArchivedCouplesUrl(userUid, coupleUid);
        mUserActiveCoupleUrl = buildActiveCoupleUrl(userUid);

        mPartnerArchivedCouplesUrl = buildArchivedCouplesUrl(partnerUid, coupleUid);
        mPartnerActiveCoupleUrl = buildActiveCoupleUrl(partnerUid);

        mCoupleUidUrl = Constraints.COUPLES + "/" + coupleUid;

        mCouple = mDatabase.child(mCoupleUidUrl);
    }

    private String buildArchivedCouplesUrl(String genericUserUid, String coupleUid) {
        return Constraints.USERS + "/" + genericUserUid + "/" + ARCHIVED_COUPLES_PARTIAL_URL + "/" + coupleUid;
    }

    private String buildActiveCoupleUrl(String genericUserUid) {
        return Constraints.USERS + "/" + genericUserUid + "/" + ACTIVE_COUPLE_PARTIAL_URL;
    }


    public void setListener(CoupleDetailsControllerListener listener) {
        mListener = listener;
    }

    public void attachCoupleListener() {
        if (mCoupleListener == null) {
            mCoupleListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onCoupleChange trigger!");
                    CoupleFB couple = dataSnapshot.getValue(CoupleFB.class);

                    if (mListener != null && couple != null) {
                        mListener.onCoupleDetailsChanged(couple);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mCouple.addValueEventListener(mCoupleListener);
        }
    }

    public void detachCoupleListener() {
        if (mCoupleListener != null) {
            mCouple.removeEventListener(mCoupleListener);
        }
        mCoupleListener = null;
    }

    public void archiveCouple() {
        // TODO: remove try catch
        String now = "no-data";
        now = DataMaker.get_UTC_DateTime();

        Map<String, Object> updates = new HashMap<>();

        // remove futurePartner field for each user
        updates.put(mUserFuturePartnerUrl, null);
        updates.put(mPartnerFuturePartnerUrl, null);

        // added the coupleUid into archived of each partner
        updates.put(mPartnerArchivedCouplesUrl, true);
        updates.put(mUserArchivedCouplesUrl, true);

        // set to null the activeCouple of each partner
        updates.put(mPartnerActiveCoupleUrl, null);
        updates.put(mUserActiveCoupleUrl, null);

        // set to false the active couple of coupleUid and push break time
        updates.put(mCoupleUidUrl + "/" + Constraints.ACTIVE, false);
        updates.put(mCoupleUidUrl + "/" + Constraints.BREAK_TIME, now);

        mDatabase.updateChildren(updates);
    }


    public void changeCoupleImage(final Uri imageLocalUri) {
        // TODO: duplicated code with ChatController
        StorageReference photoRef = mCoupleStorage.child(imageLocalUri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(imageLocalUri);
        final String imageLocalUriString = imageLocalUri.toString();

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure sendFileFirebase " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess sendImage(): " + taskSnapshot.getDownloadUrl() +"\n" + "update into uri: " + mCouple);
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String imageStorageUriString = downloadUrl.toString();

                Map<String, Object> updates = new HashMap<>();
                updates.put(Constraints.IMAGE_LOCAL_URI, imageLocalUriString);
                updates.put(Constraints.IMAGE_STORAGE_URI, imageStorageUriString);

                mCouple.updateChildren(updates);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                Log.d(TAG, "Upload image progress: " + progress);

                if (mListener != null) {
                    mListener.onImageUploadProgress((int) progress);
                }
            }
        });
    }

    public void changeAnniversaryDate(String anniversary) {
        mCouple.child(Constraints.ANNIVERSARY).setValue(anniversary);
    }

}
