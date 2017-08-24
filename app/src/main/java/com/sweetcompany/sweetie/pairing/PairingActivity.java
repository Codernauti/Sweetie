package com.sweetcompany.sweetie.pairing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sweetcompany.sweetie.firebase.FirebasePairingController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by Eduard on 29-Jun-17.
 */

public class PairingActivity extends AppCompatActivity
        implements FirebasePairingController.NewPairingListener {

    private PairingFragment mView;
    private PairingPresenter mPresenter;
    private FirebasePairingController mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pairing_activity);

        mView = (PairingFragment) getSupportFragmentManager()
                .findFragmentById(R.id.pairing_fragment_container);

        if (mView == null) {
            mView = PairingFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.pairing_fragment_container, mView);
            transaction.commit();
        }

        // get active user data
        // TODO: think to store a User json object into sharedPreferences
        String userUid = Utility.getStringPreference(this, Utility.USER_UID);
        String userUsername = Utility.getStringPreference(this, Utility.USERNAME);
        String userPhoneNumber = Utility.getStringPreference(this, Utility.PHONE_NUMBER);
        String userPairingRequestSent = Utility.getStringPreference(this, Utility.FUTURE_PARTNER_PAIRING_REQUEST);

        mController = new FirebasePairingController(userUid, userUsername);
        mPresenter = new PairingPresenter(mView, mController, userUsername, userPhoneNumber, userPairingRequestSent);

        mController.addListener(mPresenter);
        mController.setPairingListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.detachListeners();
    }

    @Override
    public void onCreateNewPairingRequestComplete(String futurePartnerUid) {
        Utility.saveStringPreference(this, Utility.FUTURE_PARTNER_PAIRING_REQUEST, futurePartnerUid);
    }
}
