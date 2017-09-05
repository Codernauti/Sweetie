package com.sweetcompany.sweetie.gallery;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.DataMaker;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Eduard on 04-Sep-17.
 */

public class GalleryInfoFragment extends Fragment implements GalleryInfoContract.View,
        View.OnClickListener {

    private static final String TAG = "GalleryInfoFragment";

    private static final int PLACE_PICKER_REQUEST = 4002;

    private GalleryInfoContract.Presenter mPresenter;

    private Toolbar mToolBar;
    private ImageView mActionImageView;
    private ProgressBar mImageUploadProgressBar;
    private TextView mImageUploadProgressText;
    private ImageButton mChangeImageButtom;
    private TextView mDateCreationTextView;
    private ImageButton mChangePositionButton;
    private TextView mPositionText;

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

        mPositionText = (TextView) root.findViewById(R.id.map_position_text);
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
        mDateCreationTextView.setText(DataMaker.get_dd_MM_yy_Local(action.getDate()));

        Glide.with(this)
                .load(action.getImageUri())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mActionImageView);

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        if(action.getLat() != null && action.getLon() != null){
            try {
                addresses = geocoder.getFromLocation(action.getLat(), action.getLon(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }


            if(addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                //String city = addresses.get(0).getLocality();
                //String state = addresses.get(0).getAdminArea();
                //String country = addresses.get(0).getCountryName();
                //String postalCode = addresses.get(0).getPostalCode();
                //String knownName = addresses.get(0).getFeatureName();

                mPositionText.setText(address);
            }
        }
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
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(getContext(), data);
            LatLng latLng;
            if (place == null) {
                Log.i(TAG, "No place selected");
                return;
            }else
            {
                latLng = place.getLatLng();
                mPresenter.changePosition(latLng.latitude, latLng.longitude);
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
                pickPosition();
                break;
            }
            default:
                break;
        }
    }

    public void pickPosition(){
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            // TODO set start bounds
            //builder.setLatLngBounds(latLngBounds);
            Intent i = builder.build(getActivity());
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            //TODO adjust catch
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
            Toast toast = new Toast(getContext());
            toast.setText("GooglePlayServices Not Available");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
            Toast toast = new Toast(getContext());
            toast.setText("Error");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
