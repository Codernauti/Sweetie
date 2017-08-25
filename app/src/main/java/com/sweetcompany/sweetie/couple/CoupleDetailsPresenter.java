package com.sweetcompany.sweetie.couple;

import android.net.Uri;
import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebaseCoupleDetailsController;
import com.sweetcompany.sweetie.model.CoupleFB;
import com.sweetcompany.sweetie.utils.DataMaker;
import com.sweetcompany.sweetie.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Eduard on 20-Jul-17.
 */

public class CoupleDetailsPresenter implements CoupleDetailsContract.Presenter,
        FirebaseCoupleDetailsController.CoupleDetailsControllerListener {

    private static final String TAG = "CoupleDetailsPresenter";

    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("d MMMM yyyy");

    private CoupleDetailsContract.View mView;
    private FirebaseCoupleDetailsController mController;


    public CoupleDetailsPresenter(CoupleDetailsContract.View view, FirebaseCoupleDetailsController controller) {
        mView = view;
        mView.setPresenter(this);

        mController = controller;
        mController.setListener(this);
    }

    @Override
    public void deleteCouple() {
        mController.archiveCouple();
    }

    @Override
    public void sendCoupleImage(Uri image) {
        mController.changeCoupleImage(image);
    }

    @Override
    public void sendNewAnniversaryData(Date newAnniversary) {
        String anniversary = DataMaker.getIsoFormatDateFromDate(newAnniversary);
        Log.d(TAG, "sendNewAnniversary: " + anniversary);
        mController.changeAnniversaryDate(anniversary);
    }


    // Controller callbacks

    @Override
    public void onCoupleDetailsChanged(CoupleFB couple) {
        //TODO extract this code into a CoupleVM? or into another utility class?
        String imageUriToLoad = getCorrectImageUri(couple);
        Log.d(TAG, "onCoupleDetailsChanged taken uri: " + imageUriToLoad);

        mView.updateCoupleData(imageUriToLoad,
                couple.getPartnerOneUsername(), couple.getPartnerTwoUsername(),
                DataMaker.getDateFromIsoFormatString(couple.getAnniversary()),
                getFormattedDate(couple.getAnniversary()),
                getFormattedDate(couple.getCreationTime())
        );
    }

    private String getCorrectImageUri(CoupleFB couple) {
        if (couple.getImageLocalUri() != null && Utility.isImageAvaibleInLocal(couple.getImageLocalUri())) {
            return couple.getImageLocalUri();
        } else {
            return couple.getImageStorageUri();
        }
    }

    private String getFormattedDate(String dateString) {
        if (dateString != null) {
            Date date = DataMaker.getDateFromIsoFormatString(dateString);
            return mDateFormat.format(date);
        } else {
            return "";
        }
    }

    @Override
    public void onImageUploadProgress(int progress) {
        mView.updateUploadProgress(progress);
    }
}
