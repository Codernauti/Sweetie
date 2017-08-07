package com.sweetcompany.sweetie.geogift;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseGeogiftController;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by ghiro on 07/08/2017.
 */

public class GeogiftActivity extends AppCompatActivity {

    private static final String TAG = "GeogiftActivity";

    // key for Intent extras
    public static final String GEOGIFT_DATABASE_KEY = "GeogiftDatabaseKey";
    public static final String GEOGIFT_TITLE = "GeogiftTitle";    // For offline user
    public static final String ACTION_DATABASE_KEY = "ActionDatabaseKey";

    private String mGeogiftKey;
    private String mActionKey;

    private GeogiftContract.Presenter mPresenter;
    private FirebaseGeogiftController mController;
    private GeogiftContract.View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geogift_activity);

        if (savedInstanceState == null) {   // first opened
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mGeogiftKey = savedInstanceState.getString(GEOGIFT_DATABASE_KEY);
            mActionKey = savedInstanceState.getString(ACTION_DATABASE_KEY);

            Log.d(TAG, "from Intent GEOGIFT_TITLE: " +
                    savedInstanceState.getString(GEOGIFT_TITLE));
            Log.d(TAG, "from Intent GEOGIFT_TITLE_DATABASE_KEY: " +
                    savedInstanceState.getString(GEOGIFT_DATABASE_KEY));
            Log.d(TAG, "from Intent GEOGIFT_TITLE_ACTION_KEY: " +
                    savedInstanceState.getString(ACTION_DATABASE_KEY));
        }
        else {
            Log.w(TAG, "No savedInstanceState or intentArgs!");
        }

        mView = (GeogiftFragment) getSupportFragmentManager()
                .findFragmentById(R.id.geogift_fragment_container);

        if (mView == null) {
            mView = GeogiftFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.geogift_fragment_container, (GeogiftFragment)mView);
            transaction.commit();
        }

        String userMail = Utility.getStringPreference(this, Utility.MAIL);
        String coupleUid = Utility.getStringPreference(this, Utility.COUPLE_UID);

        if (mGeogiftKey != null) {
            mController = new FirebaseGeogiftController(coupleUid, mGeogiftKey, mActionKey);
            mPresenter = new GeogiftPresenter(mView, mController, userMail);
        }
        else {
            Log.w(TAG, "Impossible to create GeogiftController and GeogiftPresenter because geogiftKey is NULL");
        }

        mController.attachListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mController.attachListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mController.detachListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.detachListeners();
    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GEOGIFT_DATABASE_KEY, mGeogiftKey);
        outState.putString(ACTION_DATABASE_KEY, mActionKey);
    }
}
