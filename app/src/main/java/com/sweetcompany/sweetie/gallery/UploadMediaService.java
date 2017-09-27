package com.sweetcompany.sweetie.gallery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebaseGalleryController;
import com.sweetcompany.sweetie.model.MediaFB;
import com.sweetcompany.sweetie.utils.DataMaker;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import id.zelory.compressor.Compressor;


public class UploadMediaService extends IntentService {

    private static final String TAG = "UploadMediaService";

    static final String ACTION_UID_KEY = "action";
    static final String LOCAL_FILE_PATH_KEY = "localFilePath";
    private static final long MAX_LENGTH_WITHOUT_COMPRESSION = 300000; // 300 kb

    // TODO: create a new controller specialized to upload file
    //private FirebaseGalleryController mController;

    private File mImage;


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
                File image = from(Uri.parse(localFilePath));

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


    // TODO: low level code, understand it and refactor it
    // File utils

    private File from(Uri uri) throws IOException {
        // stream source file
        InputStream sourceFileStream = getBaseContext().getContentResolver().openInputStream(uri);

        // get info of source file
        String fileName = getFileName(this, uri);
        String[] splitName = splitFileName(fileName);

        // create a new file in temporary directory with name (0) + extension (1)
        File tempFile = File.createTempFile(splitName[0], splitName[1]);

        // set settings for temporary file
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();    // delete whe app is killed

        // create a stream for the new file
        FileOutputStream tempFileStream = null;
        try {
            // bind tempFile with output stream
            tempFileStream = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (sourceFileStream != null) {
            // copy the byte from inputStream (source file) to outputStream (temporary file)
            copy(sourceFileStream, tempFileStream);
            sourceFileStream.close();
        }

        if (tempFileStream != null) {
            tempFileStream.close();
        }

        return tempFile;
    }

    private String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old " + newName + " file");
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

}
