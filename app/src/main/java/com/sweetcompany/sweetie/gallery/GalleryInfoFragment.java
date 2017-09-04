package com.sweetcompany.sweetie.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sweetcompany.sweetie.R;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Eduard on 04-Sep-17.
 */

public class GalleryInfoFragment extends Fragment implements GalleryInfoContract.View,
        View.OnClickListener {

    private static final String TAG = "GalleryInfoFragment";

    private GalleryInfoContract.Presenter mPresenter;

    private Toolbar mToolBar;
    private ImageView mActionImageView;
    private ProgressBar mImageUploadProgressBar;
    private TextView mImageUploadProgressText;
    private ImageButton mChangeImageButtom;
    private TextView mDateCreationTextView;
    private ImageButton mChangePositionButton;


    public static GalleryInfoFragment newInstance(Bundle extras) {
        GalleryInfoFragment fragment = new GalleryInfoFragment();
        fragment.setArguments(extras);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.action_info_fragment, container, false);

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.action_info_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        ActionBar actionBar = parentActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        mActionImageView = (ImageView) root.findViewById(R.id.action_info_image);
        mImageUploadProgressBar = (ProgressBar) root.findViewById(R.id.progress_bar_image_upload);
        mImageUploadProgressText = (TextView) root.findViewById(R.id.progress_image_upload);
        mChangeImageButtom = (ImageButton) root.findViewById(R.id.change_image_button);

        mDateCreationTextView = (TextView) root.findViewById(R.id.date_creation);

        mChangePositionButton = (ImageButton) root.findViewById(R.id.change_position_button);


        mChangeImageButtom.setOnClickListener(this);
        mChangePositionButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void setPresenter(GalleryInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateInfo(GalleryVM action) {
        mToolBar.setTitle(action.getTitle());
        mDateCreationTextView.setText(action.getDate());

        Glide.with(this)
                .load(action.getImageUri())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mActionImageView);

        /*
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(action.getLatitude(), action.getLongitude(), 1);

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();*/
    }

    @Override
    public void showImageUploadProgress(int progress) {
        if (progress < 100) {
            mImageUploadProgressText.setText(progress + "%");
        } else {
            setProgressViewsVisible(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                setProgressViewsVisible(true);
                Uri resultUri = result.getUri();

                mPresenter.changeImage(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, error.toString());
            }
        }
    }

    private void setProgressViewsVisible(boolean visible) {
        if (visible) {
            mImageUploadProgressBar.setVisibility(View.VISIBLE);
            mImageUploadProgressText.setVisibility(View.VISIBLE);
        } else {
            mImageUploadProgressBar.setVisibility(View.GONE);
            mImageUploadProgressText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_image_button: {

                CropImage.activity()
                        .setAspectRatio(15, 10) // a rectangle
                        .start(getContext(), this);

                break;
            }
            case R.id.change_position_button: {
                // TODO: open MapFragment for choose an address
                break;
            }
            default:
                break;
        }
    }
}
