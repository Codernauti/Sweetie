package com.sweetcompany.sweetie.pairing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sweetcompany.sweetie.BaseActivity;
import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.firebase.FirebasePairingController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;


public class PairingActivity extends BaseActivity
        implements FirebasePairingController.NewPairingListener {

    public static final String USER_FROM_REGISTER_KEY = "userFromRegister";

    private PairingFragment mView;
    private PairingPresenter mPresenter;
    private FirebasePairingController mController;

    private boolean mUserFromRegister = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pairing_activity);

        if (savedInstanceState == null) {
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mUserFromRegister = savedInstanceState.getBoolean(USER_FROM_REGISTER_KEY);
        }


        mView = (PairingFragment) getSupportFragmentManager()
                .findFragmentById(R.id.pairing_fragment_container);

        if (mView == null) {
            mView = PairingFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.pairing_fragment_container, mView);
            transaction.commit();
        }

        // get active user data
        String userUsername = Utility.getStringPreference(this, SharedPrefKeys.USERNAME);
        String userPhoneNumber = Utility.getStringPreference(this, SharedPrefKeys.PHONE_NUMBER);
        String userImageUri = Utility.getStringPreference(this, SharedPrefKeys.USER_IMAGE_URI);
        String userPairingRequestSent = Utility.getStringPreference(this, SharedPrefKeys.FUTURE_PARTNER_PAIRING_REQUEST);

        mController = new FirebasePairingController(super.mUserUid, userUsername, userImageUri);
        mPresenter = new PairingPresenter(mView, mController, userUsername, userPhoneNumber, userImageUri,
                userPairingRequestSent);

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
        Utility.saveStringPreference(this, SharedPrefKeys.FUTURE_PARTNER_PAIRING_REQUEST, futurePartnerUid);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mUserFromRegister) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(USER_FROM_REGISTER_KEY, mUserFromRegister);
    }
}
