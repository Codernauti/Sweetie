package com.sweetcompany.sweetie.registration;

import android.content.Context;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sweetcompany.sweetie.R;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;


public class StepSetUserImage extends Fragment implements RegisterContract.SetUserImageView,
        View.OnClickListener {

    static final String TAG = "StepSetUserImage";

    private RegisterContract.SetUserImagePresenter mPresenter;

    private ImageView mUserImage;
    private ProgressBar mProgressBar;
    //private TextView mProgressText;
    private ImageButton mChangeImageButton;
    private Button mForwardButton;

    public static StepSetUserImage newInstance(Bundle extras) {
        StepSetUserImage newFragment = new StepSetUserImage();
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
                showNextScreen();
                break;

            case R.id.step_three_change_img_btn:
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setMaxCropResultSize(1024, 1024)
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
                setProgressViewsVisible(true);
                Uri resultUri = result.getUri();
                mPresenter.uploadImage(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, error.toString());
            }
        }
    }

    @Override
    public void setProgressViewsVisible(boolean visible) {
        if (visible) {
            mProgressBar.setVisibility(View.VISIBLE);
            //mProgressText.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            //mProgressText.setVisibility(View.GONE);
        }
    }


    @Override
    public void setPresenter(RegisterContract.SetUserImagePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNextScreen() {
        ((RegisterActivity) getActivity()).initServiceAndOpenPairingScreen();
    }

    @Override
    public void showImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .dontAnimate()
                .placeholder(R.drawable.user_default_photo)
                .into(mUserImage);
    }
}
