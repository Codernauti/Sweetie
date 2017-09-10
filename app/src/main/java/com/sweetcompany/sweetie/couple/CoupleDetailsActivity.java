package com.sweetcompany.sweetie.couple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sweetcompany.sweetie.BaseActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseCoupleDetailsController;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by Eduard on 20-Jul-17.
 */

public class CoupleDetailsActivity extends BaseActivity {

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

        mController = new FirebaseCoupleDetailsController(super.mUserUid, super.mPartnerUid, super.mCoupleUid);
        mPresenter = new CoupleDetailsPresenter(view, mController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mController.attachCoupleListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mController.detachCoupleListener();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
