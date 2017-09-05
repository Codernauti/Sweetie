package com.sweetcompany.sweetie.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sweetcompany.sweetie.geogift.GeoItem;
import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.model.GeogiftFB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ghiro on 16/08/2017.
 */

public class FirebaseGeogiftMakerController {

    private static final String TAG = "FbGeogiftMakerController";

    private final DatabaseReference mDatabaseRef;
    private final DatabaseReference mActionsRef;
    private final DatabaseReference mGeogiftsRef;
    private final StorageReference mStorageRef;

    private final String mActionsUrl;
    private final String mGeogiftsUrl;

    private final String coupleID;
    private final String userID;

    private List<GeogiftMakerControllerListener> mListeners = new ArrayList<>();

    public interface GeogiftMakerControllerListener {
        void onMediaAdded(String uriStorage);
        void onUploadPercent(int perc);
    }


    public FirebaseGeogiftMakerController(String coupleUid, String userUID) {

        coupleID = coupleUid;
        userID = userUID;

        mActionsUrl = Constraints.ACTIONS + "/" + coupleUid;
        mGeogiftsUrl = Constraints.GEOGIFTS + "/" + coupleUid;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mActionsRef = mDatabaseRef.child(mActionsUrl);
        mGeogiftsRef = mDatabaseRef.child(mGeogiftsUrl);

        String mMediaGeogiftUrl = Constraints.GALLERY_GEOGIFTS + "/" + coupleUid;
        mStorageRef = FirebaseStorage.getInstance().getReference(mMediaGeogiftUrl);

    }

    public void addListener(FirebaseGeogiftMakerController.GeogiftMakerControllerListener listener) {
        mListeners.add(listener);
    }


    public List<String> pushGeogiftAction(ActionFB actionFB, String geogiftTitle, GeoItem geoItem) {
        List<String> newKeys =  new ArrayList<>();

        HashMap<String, Object> updates = new HashMap<>();

        DatabaseReference newGeogiftPush = mGeogiftsRef.push();
        String newGeogiftKey = newGeogiftPush.getKey();

        DatabaseReference newActionPush = mActionsRef.push();
        String newActionKey = newActionPush.getKey();

        // Set Actions
        actionFB.setChildKey(newGeogiftKey);

        // Create Gallery and set Gallery
        GeogiftFB geogiftFB = new GeogiftFB();
        geogiftFB.setActionKey(newActionKey);
        geogiftFB.setUserCreatorUID(geoItem.getUserCreatorUID());
        geogiftFB.setType(geoItem.getType());
        geogiftFB.setTitle(geogiftTitle);
        geogiftFB.setMessage(geoItem.getMessage());
        geogiftFB.setAddress(geoItem.getAddress());
        geogiftFB.setUriS(geoItem.getUriS());
        geogiftFB.setLat(geoItem.getLat());
        geogiftFB.setLon(geoItem.getLon());
        geogiftFB.setBookmarked(geoItem.isBookmarked());
        geogiftFB.setDatetimeCreation(actionFB.getLastUpdateDate());
        geogiftFB.setIsTriggered(false);

        updates.put(mActionsUrl + "/" + newActionKey, actionFB);
        updates.put(mGeogiftsUrl + "/" + newGeogiftKey, geogiftFB);

        // update database
        mDatabaseRef.updateChildren(updates);


        newKeys.add(newGeogiftKey);
        newKeys.add(newActionKey);

        return newKeys;
    }

    // push message to db and update action of this gallery
    public void uploadMedia(String uriImage) {
        Log.d(TAG, "Uploading geogift media: " + uriImage);

        Uri uriLocal = Uri.parse(uriImage);

        StorageReference photoRef = mStorageRef.child(uriLocal.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(uriLocal);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e(TAG, "onFailure sendFileFirebase " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                final String stringUriStorage;
                stringUriStorage = downloadUrl.toString();
                for (GeogiftMakerControllerListener listener : mListeners) {
                    listener.onMediaAdded(stringUriStorage);
                }
            }
        }).addOnProgressListener(
                new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        //progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        for (GeogiftMakerControllerListener listener : mListeners) {
                            listener.onUploadPercent(((int) progress));
                        }
                    }
                });
    }

}