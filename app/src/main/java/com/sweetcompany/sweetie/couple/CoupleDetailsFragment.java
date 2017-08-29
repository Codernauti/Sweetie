package com.sweetcompany.sweetie.couple;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Eduard on 20-Jul-17.
 */

public class CoupleDetailsFragment extends Fragment implements CoupleDetailsContract.View,
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "CoupleDetailsFragment";

    // TODO: extract into a general class
    private static final int RC_CODE_PICKER = 2000;

    private ImageView mCoupleImage;
    private ImageButton mChangeImageButton;
    private ProgressBar mUploadProgressBar;
    private TextView mUploadProgress;

    private TextView mCoupleNames;
    private TextView mDatePairingText;
    private TextView mAnniversaryText;
    private ImageButton mAnniversaryEditButton;

    private Button mBreakButton;

    // view cache
    // TODO: think to move this to Presenter
    private final Calendar mCalendar = Calendar.getInstance();
    private ArrayList<Image> mImagesPicked = new ArrayList<>();

    private CoupleDetailsContract.Presenter mPresenter;

    public static CoupleDetailsFragment newInstance() {
        return new CoupleDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.couple_details_fragment, container, false);

        mCoupleImage = (ImageView) root.findViewById(R.id.couple_image);
        mUploadProgressBar = (ProgressBar) root.findViewById(R.id.couple_progress_bar_image_upload);
        mUploadProgress = (TextView) root.findViewById(R.id.couple_progress_image_upload);
        mUploadProgressBar.setVisibility(View.GONE);
        mUploadProgressBar.setVisibility(View.GONE);

        mChangeImageButton = (ImageButton) root.findViewById(R.id.couple_change_image_button);

        mCoupleNames = (TextView) root.findViewById(R.id.couple_names);
        mDatePairingText = (TextView) root.findViewById(R.id.couple_date_pairing);
        mAnniversaryText = (TextView) root.findViewById(R.id.couple_anniversary);
        mAnniversaryEditButton = (ImageButton) root.findViewById(R.id.couple_edit_anniversary_button);
        mBreakButton = (Button) root.findViewById(R.id.couple_details_break_button);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.couple_details_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = parentActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mChangeImageButton.setOnClickListener(this);
        mAnniversaryEditButton.setOnClickListener(this);
        mBreakButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (requestCode == RC_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            mImagesPicked = (ArrayList<Image>) ImagePicker.getImages(data);
            if (mImagesPicked != null) {
                Uri fileUri = Uri.fromFile(new File(mImagesPicked.get(0).getPath()));
                mPresenter.sendCoupleImage(fileUri);
                mCoupleImage.setImageDrawable(null);
                showUploadProgress();
            }
        }
    }

    private void showUploadProgress() {
        mUploadProgressBar.setVisibility(View.VISIBLE);
        mUploadProgress.setVisibility(View.VISIBLE);
    }


    @Override
    public void setPresenter(CoupleDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateCoupleData(String imageUri, String partnerOneName, String partnerTwoName,
                                 Date anniversary, String anniversaryString, String creationTime) {
        Log.d(TAG, "updateCoupleData()");
        if (anniversary != null) {
            mCalendar.setTime(anniversary);
        }

        Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mCoupleImage);

        mCoupleNames.setText(partnerOneName + " " + getString(R.string.and) + " " + partnerTwoName);
        mAnniversaryText.setText(anniversaryString);
        mDatePairingText.setText(creationTime);
    }

    @Override
    public void updateUploadProgress(int progress) {
        if (progress < 100) {
            mUploadProgress.setText(progress + "%");
        } else {
            hideUploadProgress();
        }
    }

    private void hideUploadProgress() {
        mUploadProgressBar.setVisibility(View.GONE);
        mUploadProgress.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.couple_change_image_button:
                initAndStartImagePicker();
                break;

            case R.id.couple_edit_anniversary_button:
                initAndStartDatePicker();
                break;

            case R.id.couple_details_break_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("")
                        .setTitle(getString(R.string.break_couple_question))
                        .setPositiveButton(getString(R.string.break_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.deleteCouple();
                            }
                        })
                        .setNegativeButton(getString(R.string.break_no), null)
                        .create()
                        .show();
                break;
            default:
                break;
        }
    }

    private void initAndStartImagePicker() {
        ImagePicker imagePicker = ImagePicker.create(this)
                .theme(R.style.ImagePickerTheme)
                .returnAfterFirst(false) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle(String.valueOf(R.string.image_picker_select)) // image selection title
                .single()
                .limit(1) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .origin(mImagesPicked); // original selected images, used in multi mode

        imagePicker.start(RC_CODE_PICKER); // start image picker activity with request code
        // go to onActivityResult
    }

    private void initAndStartDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(getContext(), this,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));

        datePicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        mPresenter.sendNewAnniversaryData(mCalendar.getTime());
    }

}
