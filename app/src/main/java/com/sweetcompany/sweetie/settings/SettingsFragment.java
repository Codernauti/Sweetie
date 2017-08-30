package com.sweetcompany.sweetie.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sweetcompany.sweetie.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Eduard on 28-Aug-17.
 */

public class SettingsFragment extends Fragment implements SettingsContract.View, View.OnClickListener {

    private static final String TAG = "SettingsFragment";

    private SettingsContract.Presenter mPresenter;

    private ImageButton mChangeImageButton;
    private ImageView mImageView;
    private TextView mProgressImgUploadTextView;
    private ProgressBar mProgressImgUploadBar;
    private TextView mUsernameTextView;
    private TextView mEmailTextView;
    private TextView mTelephoneTextView;
    private TextView mGenderTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.settings_fragment, container, false);

        mImageView = (ImageView) root.findViewById(R.id.settings_image);
        mProgressImgUploadTextView = (TextView) root.findViewById(R.id.settings_progress_image_upload);
        mProgressImgUploadBar = (ProgressBar) root.findViewById(R.id.settings_progress_bar_image_upload);
        setProgressViewsVisible(false);

        mChangeImageButton = (ImageButton) root.findViewById(R.id.settings_change_image_button);
        mUsernameTextView = (TextView) root.findViewById(R.id.settings_username);
        mEmailTextView = (TextView) root.findViewById(R.id.settings_email);
        mTelephoneTextView = (TextView) root.findViewById(R.id.settings_phone);
        mGenderTextView = (TextView) root.findViewById(R.id.settings_gender);

        // init toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.settings_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = parentActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mChangeImageButton.setOnClickListener(this);

        return root;
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateUserInfo(String userImageUri, String username, String email, String telephone,
                               boolean gender) {
        Glide.with(getActivity())
                .load(userImageUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);

        mUsernameTextView.setText(username);
        mEmailTextView.setText(email);
        mTelephoneTextView.setText(telephone);
        mGenderTextView.setText(gender ? getString(R.string.gender_male) : getString(R.string.gender_female));
    }

    @Override
    public void updateImageUploadProgress(int progress) {
        if (progress < 100) {
            mProgressImgUploadTextView.setText(progress + "%");
        } else {
            setProgressViewsVisible(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_change_image_button:
                CropImage.activity()
                        .setAspectRatio(256,256)
                        .start(getContext(), this);
                break;
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

                // Doesn't work
                /*Glide.with(getActivity())
                        .load(resultUri)
                        .into(mImageView);*/

                mPresenter.uploadImage(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, error.toString());
            }
        }
    }

    private void setProgressViewsVisible(boolean visible) {
        if (visible) {
            mProgressImgUploadTextView.setVisibility(View.VISIBLE);
            mProgressImgUploadBar.setVisibility(View.VISIBLE);
        } else {
            mProgressImgUploadTextView.setVisibility(View.GONE);
            mProgressImgUploadBar.setVisibility(View.GONE);
        }
    }
}
