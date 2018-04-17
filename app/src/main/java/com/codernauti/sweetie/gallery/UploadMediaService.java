package com.codernauti.sweetie.gallery;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codernauti.sweetie.firebase.FirebaseGalleryController;
import com.codernauti.sweetie.model.MediaFB;
import com.codernauti.sweetie.utils.DataMaker;
import com.codernauti.sweetie.utils.FileUtility;
import com.codernauti.sweetie.utils.SharedPrefKeys;
import com.codernauti.sweetie.utils.Utility;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;


public class UploadMediaService extends IntentService {

    private static final String TAG = "UploadMediaService";

    static final String ACTION_UID_KEY = "action";
    static final String LOCAL_FILE_PATH_KEY = "localFilePath";

    private static final long MAX_LENGTH_WITHOUT_COMPRESSION = 300000; // 300 kb

    // TODO: create a new controller specialized to upload file
    //private FirebaseGalleryController mController;


    public UploadMediaService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String userUid = Utility.getStringPreference(getApplicationContext(), SharedPrefKeys.USER_UID);
            String partnerUid = Utility.getStringPreference(getApplicationContext(), SharedPrefKeys.PARTNER_UID);
            String coupleUid = Utility.getStringPreference(getApplicationContext(), SharedPrefKeys.COUPLE_UID);

            String actionUid = intent.getStringExtra(ACTION_UID_KEY);
            String localFilePath = intent.getStringExtra(LOCAL_FILE_PATH_KEY);

            MediaFB newMedia = new MediaFB(userUid, "", DataMaker.get_UTC_DateTime(), false, localFilePath, true);
            FirebaseGalleryController mController = new FirebaseGalleryController(coupleUid, actionUid, userUid, partnerUid);

            Log.d(TAG, "uploadMedia() " + actionUid + " - media: " + newMedia.getUriStorage());

            try {
                File image = FileUtility.from(this, Uri.parse(localFilePath));

                if (image.length() > MAX_LENGTH_WITHOUT_COMPRESSION) {
                    // compress image
                    File compressedImage = new Compressor(this)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(image);

                    Log.d(TAG, "AbsolutePath compressed image " + compressedImage);
                    newMedia.setUriStorage("file://" + compressedImage.getAbsolutePath());
                }

                mController.uploadMedia(newMedia);
            }
            catch (IOException ex) {
                Log.w(TAG, "Failed to compress image");
            }
        }
        else {
            Log.w(TAG, "started without an intent data");
        }
    }

}
