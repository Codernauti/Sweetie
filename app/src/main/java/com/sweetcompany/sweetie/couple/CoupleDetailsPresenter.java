package com.sweetcompany.sweetie.couple;

import android.net.Uri;
import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebaseCoupleDetailsController;
import com.sweetcompany.sweetie.model.CoupleFB;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by Eduard on 20-Jul-17.
 */

public class CoupleDetailsPresenter implements CoupleDetailsContract.Presenter,
        FirebaseCoupleDetailsController.CoupleDetailsControllerListener {

    private static final String TAG = "CoupleDetailsPresenter";

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


    // Controller callbacks

    @Override
    public void onCoupleDetailsChanged(CoupleFB couple) {
        //TODO extract this code into a CoupleVM? or into another utility class?
        String imageUriToLoad = getCorrectImageUri(couple);
        Log.d(TAG, "onCoupleDetailsChanged taken uri: " + imageUriToLoad);

        mView.updateCoupleData(imageUriToLoad, couple.getPartnerOneUsername(), couple.getPartnerTwoUsername(),
                    couple.getAnniversary(), couple.getCreationTime());
    }

    private String getCorrectImageUri(CoupleFB couple) {
        if (couple.getImageLocalUri() != null && Utility.isImageAvaibleInLocal(couple.getImageLocalUri())) {
            return couple.getImageLocalUri();
        } else {
            return couple.getImageStorageUri();
        }
    }

    @Override
    public void onImageUploadProgress(int progress) {
        mView.updateUploadProgress(progress);
    }
}
