package com.sweetcompany.sweetie.couple;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.pairing.PairingActivity;
import com.sweetcompany.sweetie.utils.Utility;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Eduard on 20-Jul-17.
 */

public class CoupleDetailsFragment extends Fragment implements CoupleDetailsContract.View,
        View.OnClickListener {

    // TODO: extract into a general class
    private static final int RC_CODE_PICKER = 2000;

    private ImageView mCoupleImage;
    private ImageButton mChangeImageButton;
    private TextView mCoupleNames;
    private TextView mDatePairingText;
    private TextView mAnniversaryText;
    private Button mFindPartnerButton;
    private Button mBreakButton;

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
        mChangeImageButton = (ImageButton) root.findViewById(R.id.couple_change_image_button);
        mCoupleNames = (TextView) root.findViewById(R.id.couple_names);
        mDatePairingText = (TextView) root.findViewById(R.id.couple_date_pairing);
        mAnniversaryText = (TextView) root.findViewById(R.id.couple_anniversary);
        mFindPartnerButton = (Button) root.findViewById(R.id.couple_details_find_partner_button);
        mBreakButton = (Button) root.findViewById(R.id.couple_details_break_button);

        mChangeImageButton.setOnClickListener(this);
        mFindPartnerButton.setOnClickListener(this);
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
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void setPresenter(CoupleDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateCoupleData(String imageUri, String partnerOneName, String partnerTwoName, String anniversary, String creationTime) {
        Glide.with(getContext())
                .load(imageUri)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mCoupleImage);

        mCoupleNames.setText(partnerOneName + " " + getString(R.string.and) + " " + partnerTwoName);
        mAnniversaryText.setText(anniversary);
        mDatePairingText.setText(creationTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.couple_change_image_button:
                initAndStartImagePicker();
                break;
            case R.id.couple_edit_anniversary_button:
                // TODO: change TextView into an EditText
                break;
            case R.id.couple_details_break_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage("")
                        .setTitle("Are you sure?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.deleteCouple();
                            }
                        })
                        .setNegativeButton("Back", null)
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
                .limit(10) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .origin(mImagesPicked); // original selected images, used in multi mode

        imagePicker.start(RC_CODE_PICKER); // start image picker activity with request code
        // go to onActivityResult
    }
}
