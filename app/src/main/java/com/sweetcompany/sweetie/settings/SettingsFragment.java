package com.sweetcompany.sweetie.settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sweetcompany.sweetie.GeogiftMonitorService;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.geogift.GeofenceTrasitionService;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;


public class SettingsFragment extends Fragment implements SettingsContract.View,
        View.OnClickListener {

    private static final String TAG = "SettingsFragment";
    private static final int PERMISSION_ACCESS_FINE_LOCATION_CODE = 100;

    private SettingsContract.Presenter mPresenter;

    private ImageButton mChangeImageButton;
    private ImageView mImageView;
    private TextView mProgressImgUploadTextView;
    private ProgressBar mProgressImgUploadBar;

    private TextView mUsernameTextView;
    private TextView mEmailTextView;
    private TextView mTelephoneTextView;
    private TextView mGenderTextView;

    private Switch mGeogiftSwitch;
    private boolean mGeogiftWasEnabled;

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

        mGeogiftSwitch = (Switch) root.findViewById(R.id.settings_geogift_switch);

        mGeogiftWasEnabled = Utility.getBooleanPreference(getContext(), SharedPrefKeys.Options.GEOGIFT_ENABLED);
        mGeogiftSwitch.setChecked(mGeogiftWasEnabled);

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
        mGeogiftSwitch.setOnClickListener(this);

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

        if (mProgressImgUploadBar.getVisibility() == View.GONE) {
            // if progressBar is hide, load image
            Glide.with(getActivity())
                    .load(userImageUri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mImageView);
        }

        mUsernameTextView.setText(username);
        mEmailTextView.setText(email);
        mTelephoneTextView.setText(telephone);
        mGenderTextView.setText(gender ? getString(R.string.gender_male) : getString(R.string.gender_female));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_change_image_button:
                CropImage.activity()
                        .setAspectRatio(256,256)
                        .start(getContext(), this);
                break;

            case R.id.settings_geogift_switch:

                if (mGeogiftWasEnabled) {
                    setGeogiftFeature(false);
                } else {
                    Log.d(TAG, "ask permission");

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                                PERMISSION_ACCESS_FINE_LOCATION_CODE);
                        // go to onRequestPermissionsResult()
                    } else {
                        // permission yet given
                        setGeogiftFeature(true);
                    }
                }
            default:
                break;
        }
    }

    private void setGeogiftFeature(boolean enable) {
        if (enable) {
            Log.d(TAG, "enable geogift");
            mGeogiftWasEnabled = true;
            Utility.saveBooleanPreference(getContext(), SharedPrefKeys.Options.GEOGIFT_ENABLED, true);
            getActivity().startService(new Intent(getContext(), GeogiftMonitorService.class));
        } else {
            Log.d(TAG, "disable geogift");
            mGeogiftWasEnabled = false;
            Utility.saveBooleanPreference(getContext(), SharedPrefKeys.Options.GEOGIFT_ENABLED, false);
            getActivity().stopService(new Intent(getContext(), GeogiftMonitorService.class));
            getActivity().stopService(new Intent(getContext(), GeofenceTrasitionService.class));
            // TODO: unregister the geogift.
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                setProgressViewsVisible(true);
                Uri resultUri = result.getUri();

                // Immediately feedback the user
                Glide.with(getActivity())
                        .load(resultUri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mImageView);

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
            //mProgressImgUploadTextView.setVisibility(View.VISIBLE);
            mProgressImgUploadBar.setVisibility(View.VISIBLE);
        } else {
            //mProgressImgUploadTextView.setVisibility(View.GONE);
            mProgressImgUploadBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permission given");
                    setGeogiftFeature(true);
                } else {
                    Log.d(TAG, "permission not give");
                    setGeogiftFeature(false);
                    mGeogiftSwitch.setChecked(false);
                    Toast.makeText(getContext(), getString(R.string.app_permission_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }
}
