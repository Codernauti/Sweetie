package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eduard on 20-Jul-17.
 */

public class FirebaseCoupleDetailsController {

    private static final String TAG = "CoupleDetailsController";

    private static final String ACTIVE_COUPLE_PARTIAL_URL = Constraints.COUPLE_INFO_NODE + "/" + Constraints.ACTIVE_COUPLE;
    private static final String ARCHIVED_COUPLES_PARTIAL_URL = Constraints.COUPLE_INFO_NODE + "/" + Constraints.ARCHIVED_COUPLES;

    private final DatabaseReference mDatabase;

    private final String mUserArchivedCouplesUrl;       // users/<userUid>/coupleInfo/activeCouple/<coupleUid>
    private final String mUserActiveCoupleUrl;          // users/<userUid>/coupleInfo/archivedCouples
    private final String mPartnerArchivedCouplesUrl;    // users/<partnerUid>/coupleInfo/activeCouple/<coupleUid>
    private final String mPartnerActiveCoupleUrl;       // users/<partnerUid>/coupleInfo/archivedCouples
    private final String mCoupleUidUrl;                 // couples/<coupleUid>

    public FirebaseCoupleDetailsController(String userUid, String partnerUid, String coupleUid) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUserArchivedCouplesUrl = buildArchivedCouplesUrl(userUid, coupleUid);
        mUserActiveCoupleUrl = buildActiveCoupleUrl(userUid);

        mPartnerArchivedCouplesUrl = buildArchivedCouplesUrl(partnerUid, coupleUid);
        mPartnerActiveCoupleUrl = buildActiveCoupleUrl(partnerUid);

        mCoupleUidUrl = Constraints.COUPLES_NODE + "/" + coupleUid;
    }

    private String buildArchivedCouplesUrl(String genericUserUid, String coupleUid) {
        return Constraints.USERS_NODE + "/" + genericUserUid + "/" + ARCHIVED_COUPLES_PARTIAL_URL + "/" + coupleUid;
    }
    private String buildActiveCoupleUrl(String genericUserUid) {
        return Constraints.USERS_NODE + "/" + genericUserUid + "/" + ACTIVE_COUPLE_PARTIAL_URL;
    }


    public void archiveCouple() {
        // TODO: remove try catch
        String now = "no-data";
        try {
            now = DataMaker.get_UTC_DateTime();
        }
        catch(ParseException ex) {
            Log.d(TAG, ex.getMessage());
        }

        Map<String, Object> updates = new HashMap<>();

        // added the coupleUid into archived of each partner
        // TODO: don't work
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

}
