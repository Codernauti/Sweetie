package com.sweetcompany.sweetie.actionInfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.DataMaker;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Eduard on 05-Sep-17.
 */

public class ActionInfoFragment extends Fragment implements ActionInfoContract.View,
        View.OnClickListener {

    private static final String SUPER_TAG = "ActionInfoFragment";

    private ActionInfoContract.Presenter mPresenter;

    private Toolbar mToolBar;
    private ImageView mActionImageView;
    private ProgressBar mImageUploadProgressBar;
    private TextView mImageUploadProgressText;
    private ImageButton mChangeImageButtom;
    private TextView mDateCreationTextView;

    protected ImageButton sChangePositionButton;
    protected TextView sPositionText;

    private RelativeLayout mPositionLayout;

    public static ActionInfoFragment newInstance(Bundle extras) {
        ActionInfoFragment fragment = new ActionInfoFragment();
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

        // by default is gone
        mPositionLayout = (RelativeLayout) root.findViewById(R.id.position_layout);
        sPositionText = (TextView) root.findViewById(R.id.map_position_text);
        sChangePositionButton = (ImageButton) root.findViewById(R.id.change_position_button);
        mPositionLayout.setVisibility(View.GONE);


        mChangeImageButtom.setOnClickListener(this);
        sChangePositionButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void setPresenter(ActionInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateInfo(ActionInfoVM action) {
        mToolBar.setTitle(action.getTitle());

        mDateCreationTextView.setText(DataMaker.getDate_d_month_yyyy(action.getCreationDate()));

        Glide.with(this)
                .load(action.getUriCover())
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(mActionImageView);

        if (action.isUploadingImg()) {
            setProgressViewsVisible(true);
        } else {
            setProgressViewsVisible(false);
        }

        showImageUploadProgress(action.getProgress());
    }

    private void showImageUploadProgress(int progress) {
        if (progress < 100) {
            mImageUploadProgressText.setText(progress + "%");
        }
    }

    @CallSuper
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
                Log.d(SUPER_TAG, error.toString());
            }
        }
        // call by subclass
        /*if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(getContext(), data);
            LatLng latLng;
            if (place == null) {
                Log.i(SUPER_TAG, "No place selected");
                return;
            }else
            {
                latLng = place.getLatLng();
                mPresenter.changePosition(latLng.latitude, latLng.longitude);
            }
        }*/
    }

    private void setProgressViewsVisible(boolean visible) {
        if (visible) {
            mImageUploadProgressBar.setVisibility(View.VISIBLE);
            mImageUploadProgressText.setVisibility(View.VISIBLE);
            mChangeImageButtom.setEnabled(false);
        } else {
            mImageUploadProgressBar.setVisibility(View.GONE);
            mImageUploadProgressText.setVisibility(View.GONE);
            mChangeImageButtom.setEnabled(true);
        }
    }

    public void setPositionLayoutVisible() {
        mPositionLayout.setVisibility(View.VISIBLE);
    }

    @CallSuper
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_image_button: {

                CropImage.activity()
                        .setAspectRatio(13, 10) // a rectangle
                        .start(getContext(), this);

                break;
            }
            /*case R.id.change_position_button: {
                pickPosition();
                break;
            }*/
            default:
                break;
        }
    }

    /*public void pickPosition(){
        try {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            // TODO set start bounds
            //builder.setLatLngBounds(latLngBounds);
            Intent i = builder.build(getActivity());
            startActivityForResult(i, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            //TODO adjust catch
            Log.e(SUPER_TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
            Toast.makeText(getContext(), "GooglePlayServices Not Available", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(SUPER_TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }*/
}
