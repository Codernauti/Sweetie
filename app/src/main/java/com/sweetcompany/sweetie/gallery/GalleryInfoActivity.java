package com.sweetcompany.sweetie.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.sweetcompany.sweetie.BaseActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseActionInfoController;
import com.sweetcompany.sweetie.model.GalleryFB;

import static com.sweetcompany.sweetie.gallery.GalleryActivity.GALLERY_TITLE;

/**
 * Created by Eduard on 04-Sep-17.
 */

public class GalleryInfoActivity extends BaseActivity {

    private static final String TAG = "GalleryInfoActivity";

    private static final String GALLERY_UID_KEY = "galleryUid";
    private static final String PARENT_ACTION_UID_KEY = "parentActionUid";

    private String mGalleryUid;
    private String mParentActionUid;

    private FirebaseActionInfoController<GalleryFB> mController;
    private GalleryInfoContract.Presenter mPresenter;

    public static Intent getStartActivityIntent(Context context, String galleryUid, String parentActionUid) {
        Intent intent = new Intent(context, GalleryInfoActivity.class);
        intent.putExtra(GALLERY_UID_KEY, galleryUid);
        intent.putExtra(PARENT_ACTION_UID_KEY, parentActionUid);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_info_activity);


        if (savedInstanceState == null) {   // first opened
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mGalleryUid = savedInstanceState.getString(GALLERY_UID_KEY);
            mParentActionUid = savedInstanceState.getString(PARENT_ACTION_UID_KEY);

            Log.d(TAG, "GalleryUid = " + mGalleryUid + "\n" + "ParentActionUid = " + mParentActionUid);
        }
        else {
            Log.w(TAG, "No savedInstanceState or intentArgs!");
        }


        GalleryInfoFragment view = (GalleryInfoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gallery_info_fragment_container);

        if (view == null) {
            view = GalleryInfoFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.gallery_info_fragment_container, view);
            transaction.commit();
        }

        if (mGalleryUid != null) {
            mController = new FirebaseActionInfoController<>(super.mCoupleUid,
                    mParentActionUid, mGalleryUid, GalleryFB.class);
            mPresenter = new GalleryInfoPresenter(view, mController);
        } else {
            Log.w(TAG, "Gallery Uid is null, impossible to instantiate Controller and presenter");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mController.attachListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mController.detachListener();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(mGalleryUid, GALLERY_UID_KEY);
        outState.putString(mParentActionUid, PARENT_ACTION_UID_KEY);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
