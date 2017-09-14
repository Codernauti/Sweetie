package com.sweetcompany.sweetie.settings;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.sweetcompany.sweetie.BaseActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseSettingsController;


public class SettingsActivity extends BaseActivity {

    private SettingsFragment mView;
    private SettingsPresenter mPresenter;
    private FirebaseSettingsController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        mView = (SettingsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.settings_fragment_container);

        if (mView == null) {
            mView = SettingsFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.settings_fragment_container, mView);
            transaction.commit();
        }

        mController = new FirebaseSettingsController(super.mUserUid);
        mPresenter = new SettingsPresenter(mView, mController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mController.attachListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mController.detachListener();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
