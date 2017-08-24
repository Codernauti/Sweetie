package com.sweetcompany.sweetie.couple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseCoupleDetailsController;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by Eduard on 20-Jul-17.
 */

public class CoupleDetailsActivity extends AppCompatActivity {

    CoupleDetailsContract.Presenter mPresenter;
    FirebaseCoupleDetailsController mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.couple_details_activity);

        CoupleDetailsFragment view = (CoupleDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.couple_details_fragment_container);

        if (view == null) {
            view = CoupleDetailsFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.couple_details_fragment_container, view);
            transaction.commit();
        }

        String userUid = Utility.getStringPreference(this, Utility.USER_UID);
        String partnerUid = Utility.getStringPreference(this, Utility.FUTURE_PARTNER_PAIRING_REQUEST);
        String coupleUid = Utility.getStringPreference(this, Utility.COUPLE_UID);

        mController = new FirebaseCoupleDetailsController(userUid, partnerUid, coupleUid);
        mPresenter = new CoupleDetailsPresenter(view, mController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mController.attachCoupleListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mController.detachCoupleListener();
    }
}
