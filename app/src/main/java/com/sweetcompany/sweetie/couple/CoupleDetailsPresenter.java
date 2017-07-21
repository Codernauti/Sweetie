package com.sweetcompany.sweetie.couple;

import com.sweetcompany.sweetie.firebase.FirebaseCoupleDetailsController;

/**
 * Created by Eduard on 20-Jul-17.
 */

public class CoupleDetailsPresenter implements CoupleDetailsContract.Presenter {

    private CoupleDetailsContract.View mView;
    private FirebaseCoupleDetailsController mController;


    public CoupleDetailsPresenter(CoupleDetailsContract.View view, FirebaseCoupleDetailsController controller) {
        mView = view;
        mView.setPresenter(this);
        mController = controller;
    }

    @Override
    public void deleteCouple() {
        mController.archiveCouple();
    }

}
