package com.sweetcompany.sweetie.Registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sweetcompany.sweetie.Firebase.FirebasePairingController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;

/**
 * Created by Eduard on 29-Jun-17.
 */

public class PairingActivity extends AppCompatActivity {

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
        String userUid = Utility.getStringPreference(this, Utility.USER_UID);
        String userPhoneNumber = Utility.getStringPreference(this, Utility.PHONE_NUMBER);

        mController = new FirebasePairingController(userUid);
        mPresenter = new PairingPresenter(mView, mController, userUid, userPhoneNumber);

        mController.addListener(mPresenter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.detachFromFirebase();
    }
}
