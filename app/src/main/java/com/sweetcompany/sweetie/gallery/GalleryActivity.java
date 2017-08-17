package com.sweetcompany.sweetie.gallery;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.sweetcompany.sweetie.R;
import android.util.Log;
import com.sweetcompany.sweetie.firebase.FirebaseGalleryController;
import com.sweetcompany.sweetie.utils.Utility;

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";

    // key for Intent extras
    public static final String GALLERY_DATABASE_KEY = "GalleryDatabaseKey";
    public static final String GALLERY_TITLE = "GalleryTitle";    // For offline user
    public static final String ACTION_DATABASE_KEY = "ActionDatabaseKey";

    private String mGalleryKey;
    private String mActionKey;

    private GalleryPresenter mPresenter;
    private FirebaseGalleryController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);

        if (savedInstanceState == null) {   // first opened
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mGalleryKey = savedInstanceState.getString(GALLERY_DATABASE_KEY);
            mActionKey = savedInstanceState.getString(ACTION_DATABASE_KEY);

            Log.d(TAG, "from Intent GALLERY_TITLE: " +
                    savedInstanceState.getString(GALLERY_TITLE));
            Log.d(TAG, "from Intent GALLERY_TITLE_DATABASE_KEY: " +
                    savedInstanceState.getString(GALLERY_DATABASE_KEY));
            Log.d(TAG, "from Intent GALLERY_TITLE_ACTION_KEY: " +
                    savedInstanceState.getString(ACTION_DATABASE_KEY));
        }
        else {
            Log.w(TAG, "No savedInstanceState or intentArgs!");
        }

        GalleryFragment view = (GalleryFragment) getSupportFragmentManager()
                               .findFragmentById(R.id.gallery_fragment_container);

        if (view == null) {
            view = GalleryFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.gallery_fragment_container, view);
            transaction.commit();
        }

        String userMail = Utility.getStringPreference(this, Utility.MAIL);
        String coupleUid = Utility.getStringPreference(this, Utility.COUPLE_UID);

        if (mGalleryKey != null) {
            mController = new FirebaseGalleryController(coupleUid, mGalleryKey, mActionKey);
            mPresenter = new GalleryPresenter(view, mController, userMail);
        }
        else {
            Log.w(TAG, "Impossible to create GalleryController and GalleryPresenter because galleryKey is NULL");
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
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GALLERY_DATABASE_KEY, mGalleryKey);
        outState.putString(ACTION_DATABASE_KEY, mActionKey);
    }
}

