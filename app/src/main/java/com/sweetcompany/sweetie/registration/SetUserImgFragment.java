package com.sweetcompany.sweetie.registration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;


public class SetUserImgFragment extends Fragment implements View.OnClickListener {

    static final String TAG = "SetUserImgFragment";

    private ImageView mUserImage;
    private ProgressBar mProgressBar;
    //private TextView mProgressText;
    private ImageButton mChangeImageButton;
    private Button mForwardButton;

    public static SetUserImgFragment newInstance(Bundle extras) {
        SetUserImgFragment newFragment = new SetUserImgFragment();
        newFragment.setArguments(extras);
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.register_step_set_user_image, container, false);

        // Assign fields
        mUserImage = (ImageView) root.findViewById(R.id.step_three_user_image);
        mProgressBar = (ProgressBar) root.findViewById(R.id.step_three_progress_bar_img_upload);
        //mProgressText = (TextView) root.findViewById(R.id.step_three_progress_img_upload);
        mChangeImageButton = (ImageButton) root.findViewById(R.id.step_three_change_img_btn);
        mForwardButton = (Button) root.findViewById(R.id.step_three_next_btn);

        showImage(Utility.getStringPreference(getContext(), SharedPrefKeys.USER_IMAGE_URI));

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.step_three_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = parentActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Set click listeners
        mChangeImageButton.setOnClickListener(this);
        mForwardButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_three_next_btn:
                // showNextScreen();
                ((RegisterActivity)getActivity()).registrationComplete();
                break;

            case R.id.step_three_change_img_btn:
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setMaxCropResultSize(2048, 2048)
                        .start(getContext(), this);
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                //setProgressViewsVisible(true);
                Uri resultUri = result.getUri();

                Utility.saveStringPreference(getContext(), SharedPrefKeys.USER_IMAGE_URI, resultUri.toString());

                showImage(resultUri.toString());
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, error.toString());
            }
        }
    }

    private void showImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_default_photo)
                .into(mUserImage);
    }
}
