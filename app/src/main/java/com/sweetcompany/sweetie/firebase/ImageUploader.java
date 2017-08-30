package com.sweetcompany.sweetie.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Eduard on 30-Aug-17.
 */

class ImageUploader {

    interface OnImageUploadProgressListener {
        void onImageUploadProgress(int progress);
    }

    static ImageUploaderBuilder build(StorageReference reference) {
        return new ImageUploaderBuilder(reference);
    }



    static final class ImageUploaderBuilder {

        private OnImageUploadProgressListener mListener;

        private OnFailureListener mFailureListener;
        private OnSuccessListener<UploadTask.TaskSnapshot> mSuccessListener;
        private OnProgressListener<UploadTask.TaskSnapshot> mProgressListener;
        private final StorageReference mStorageRef;
        private String classTag = "ImageUploaderDefaultTag";

        private ImageUploaderBuilder(StorageReference reference) {
            mStorageRef = reference;
        }

        ImageUploaderBuilder setOnFailureListener(OnFailureListener listener) {
            mFailureListener = listener;
            return this;
        }

        ImageUploaderBuilder setOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> listener) {
            mSuccessListener = listener;
            return this;
        }

        ImageUploaderBuilder setOnProgressListener(OnProgressListener<UploadTask.TaskSnapshot> listener) {
            mProgressListener = listener;
            return this;
        }

        ImageUploaderBuilder setDefaultImageUploadProgressListener(OnImageUploadProgressListener listener) {
            mListener = listener;
            return this;
        }

        void startUpload(Uri imgLocalUri) {
            UploadTask uploadTask = mStorageRef.putFile(imgLocalUri);

            if (mFailureListener != null) {
                uploadTask.addOnFailureListener(mFailureListener);
            } else {
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(classTag, "onFailure sendFileFirebase " + exception.getMessage());
                    }
                });
            }

            if (mSuccessListener != null) {
                uploadTask.addOnSuccessListener(mSuccessListener);
            }

            if (mProgressListener != null) {
                uploadTask.addOnProgressListener(mProgressListener);
            } else {
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        Log.d(classTag, "Upload image progress: " + progress);

                        if (mListener != null) {
                            mListener.onImageUploadProgress((int) progress);
                        }
                    }
                });
            }
        }
    }
}
