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
import java.util.List;

/**
 * Created by ghiro on 16/08/2017.
 */

public class FirebaseGeogiftMakerController {

    private static final String TAG = "FbGeogiftMakerController";

    private final DatabaseReference mActionsDbReference;
    private final DatabaseReference mGeogiftDbReference;
    private final StorageReference mStorageRef;
    private final FirebaseStorage mStorage;

    private final String coupleID;
    private final String userID;

    private List<GeogiftMakerControllerListener> mListeners = new ArrayList<>();

    public interface GeogiftMakerControllerListener {
        void onMediaAdded(String uriStorage);
        void onUploadPercent(int perc);
    }


    public FirebaseGeogiftMakerController(String coupleUid, String userUID) {
        mActionsDbReference = FirebaseDatabase.getInstance().getReference(Constraints.ACTIONS + "/" + coupleUid);
        mGeogiftDbReference = FirebaseDatabase.getInstance().getReference(Constraints.GEOGIFTS + "/" + coupleUid);
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        coupleID = coupleUid;
        userID = userUID;
    }

    public void addListener(FirebaseGeogiftMakerController.GeogiftMakerControllerListener listener) {
        mListeners.add(listener);
    }


    public List<String> pushGeogiftAction(ActionFB actionFB, String geogiftTitle, GeoItem geoItem) {
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
        geogift.setKey(newGeogiftKey);
        geogift.setUserCreatorUID(geoItem.getUserCreatorUID());
        geogift.setType(geoItem.getType());
        geogift.setMessage(geoItem.getMessage());
        geogift.setAddress(geoItem.getAddress());
        geogift.setUriS(geoItem.getUriS());
        geogift.setLat(geoItem.getLat());
        geogift.setLon(geoItem.getLon());
        geogift.setBookmarked(geoItem.isBookmarked());
        geogift.setDatetimeCreation(actionFB.getDataTime());
        geogift.setVisited(false);

        // put into queue for network
        newGeogiftPush.setValue(geogift);
        newActionPush.setValue(actionFB);

        return newKeys;
    }

    // push message to db and update action of this gallery
    public void uploadMedia(String uriImage) {
        Log.d(TAG, "Upload geogift media: " + uriImage);

        Uri uriLocal;
        uriLocal = Uri.parse(uriImage);

        StorageReference photoRef = mStorageRef.child(Constraints.GALLERY_GEOGIFTS+coupleID+"/"+uriLocal.getLastPathSegment());
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