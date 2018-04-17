package com.codernauti.sweetie.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.codernauti.sweetie.BaseActivity;
import com.codernauti.sweetie.R;
import com.codernauti.sweetie.actionInfo.ActionInfoContract;
import com.codernauti.sweetie.actionInfo.ActionInfoFragment;
import com.codernauti.sweetie.actionInfo.ActionInfoPresenter;
import com.codernauti.sweetie.firebase.FirebaseActionInfoController;
import com.codernauti.sweetie.model.GalleryFB;


public class GalleryInfoActivity extends BaseActivity {

    private static final String TAG = "GalleryInfoActivity";

    private static final String GALLERY_UID_KEY = "galleryUid";

    private String mGalleryUid;

    private FirebaseActionInfoController<GalleryFB> mController;
    private ActionInfoContract.Presenter mPresenter;

    public static Intent getStartActivityIntent(Context context, String galleryUid) {
        Intent intent = new Intent(context, GalleryInfoActivity.class);
        intent.putExtra(GALLERY_UID_KEY, galleryUid);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_info_activity);


        if (savedInstanceState == null) {   // first opened
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mGalleryUid = savedInstanceState.getString(GALLERY_UID_KEY);

            Log.d(TAG, "GalleryUid = " + mGalleryUid);
        }
        else {
            Log.w(TAG, "No savedInstanceState or intentArgs!");
        }


        ActionInfoFragment view = (ActionInfoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.action_info_fragment_container);

        if (view == null) {
            view = ActionInfoFragment.newInstance(getIntent().getExtras());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.action_info_fragment_container, view);
            transaction.commit();
        }

        if (mGalleryUid != null) {
            mController = new FirebaseActionInfoController<>(super.mCoupleUid, mGalleryUid, GalleryFB.class);
            mPresenter = new ActionInfoPresenter<GalleryFB>(view, mController);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GALLERY_UID_KEY, mGalleryUid);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
