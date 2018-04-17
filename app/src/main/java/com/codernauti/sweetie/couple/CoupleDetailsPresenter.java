package com.codernauti.sweetie.couple;

import android.net.Uri;
import android.util.Log;

import com.codernauti.sweetie.firebase.FirebaseCoupleDetailsController;
import com.codernauti.sweetie.model.CoupleFB;
import com.codernauti.sweetie.utils.DataMaker;

import java.text.SimpleDateFormat;
import java.util.Date;


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
        String imageUriToLoad = couple.getImageStorageUri();
        Log.d(TAG, "onCoupleDetailsChanged taken uri: " + imageUriToLoad);

        Date anniversary = null;
        if (couple.getAnniversary() != null) {
            anniversary = DataMaker.getDateFromIsoFormatString(couple.getAnniversary());
        }

        if (couple.isUploadingImg()) {
            mView.updateUploadProgress(couple.getProgress());
        } else {
            mView.hideUploadProgress();
        }

        mView.updateCoupleData(imageUriToLoad,
                couple.getPartnerOneUsername(), couple.getPartnerTwoUsername(),
                anniversary,
                getFormattedDate(couple.getAnniversary()),
                getFormattedDate(couple.getCreationTime())
        );

    }

    private String getFormattedDate(String dateString) {
        if (dateString != null) {
            Date date = DataMaker.getDateFromIsoFormatString(dateString);
            return mDateFormat.format(date);
        } else {
            return "";
        }
    }
}
