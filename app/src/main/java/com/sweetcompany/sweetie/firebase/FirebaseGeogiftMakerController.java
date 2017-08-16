package com.sweetcompany.sweetie.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.model.GeogiftFB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 16/08/2017.
 */

public class FirebaseGeogiftMakerController {

    private static final String TAG = "FbGeogiftMakerController";

    private final DatabaseReference mActionsDbReference;
    private final DatabaseReference mGeogiftDbReference;


    public FirebaseGeogiftMakerController(String coupleUid) {
        mActionsDbReference = FirebaseDatabase.getInstance().getReference(Constraints.ACTIONS + "/" + coupleUid);
        mGeogiftDbReference = FirebaseDatabase.getInstance().getReference(Constraints.GEOGIFT + "/" + coupleUid);
    }

    public List<String> pushGeogiftAction(ActionFB actionFB, String geogiftTitle) {
        List<String> newKeys =  new ArrayList<String>();

        DatabaseReference newGeogiftPush = mGeogiftDbReference.push();
        String newGeogiftKey = newGeogiftPush.getKey();
        newKeys.add(newGeogiftKey);
        DatabaseReference newActionPush = mActionsDbReference.push();
        String newActionKey = newActionPush.getKey();
        newKeys.add(newActionKey);

        // Set Action
        actionFB.setChildKey(newGeogiftKey);

        // Create Gallery and set Gallery
        GeogiftFB geogift = new GeogiftFB();
        geogift.setTitle(geogiftTitle);

        // put into queue for network
        newGeogiftPush.setValue(geogift);
        newActionPush.setValue(actionFB);

        return newKeys;
    }

}