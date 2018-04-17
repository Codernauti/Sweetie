package com.codernauti.sweetie.geogift;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.codernauti.sweetie.BaseActivity;
import com.codernauti.sweetie.R;
import com.codernauti.sweetie.firebase.FirebaseGeogiftDoneController;

public class GeogiftDoneActivity extends BaseActivity {

    private static final String TAG = "GeogiftDoneActivity";

    // key for Intent extras
    public static final String GEOGIFT_DATABASE_KEY = "GeogiftDatabaseKey";

    private String mGeogiftKey;

    private GeogiftDoneFragment mView;

    private GeogiftDonePresenter mPresenter;
    private FirebaseGeogiftDoneController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geogift_done_activity);

        if (savedInstanceState == null) {   // first opened
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mGeogiftKey = savedInstanceState.getString(GEOGIFT_DATABASE_KEY);

            Log.d(TAG, "from Intent GEOGIFT_TITLE_DATABASE_KEY: " +
                    savedInstanceState.getString(GEOGIFT_DATABASE_KEY));
        }
        else {
            Log.w(TAG, "No savedInstanceState or intentArgs!");
        }

        mView = (GeogiftDoneFragment) getSupportFragmentManager()
                .findFragmentById(R.id.geogift_done_fragment_container);

        if (mView == null) {
            mView = GeogiftDoneFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.geogift_done_fragment_container, mView);
            transaction.commit();
        }

        if (mGeogiftKey != null) {
            mController = new FirebaseGeogiftDoneController(super.mCoupleUid, mGeogiftKey);
            mPresenter = new GeogiftDonePresenter(mView, mController, super.mUserUid);
        }
        else {
            Log.w(TAG, "Impossible to create GeogiftDoneController and GeogiftDonePresenter because geogiftKey is NULL");
        }

        mController.attachListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.detachListeners();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GEOGIFT_DATABASE_KEY, mGeogiftKey);
    }
}