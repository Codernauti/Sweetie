package com.codernauti.sweetie.geogift;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.codernauti.sweetie.BaseActivity;
import com.codernauti.sweetie.R;
import com.codernauti.sweetie.firebase.FirebaseGeogiftMakerController;


public class GeogiftMakerActivity extends BaseActivity {
    private static final String TAG = "GeogiftMakerActivity";

    // key for Intent extras
    public static final String GEOGIFT_TITLE = "GeogiftTitle";

    private GeogiftMakerFragment mView;

    private GeogiftMakerPresenter mPresenter;
    private FirebaseGeogiftMakerController mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geogift_maker_activity);

        if (savedInstanceState == null) {   // first opened
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            Log.d(TAG, "from Intent GEOGIFT_TITLE: " +
                    savedInstanceState.getString(GEOGIFT_TITLE));
        }
        else {
            Log.w(TAG, "No savedInstanceState or intentArgs!");
        }

        mView = (GeogiftMakerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.geogift_maker_fragment_container);

        if (mView == null) {
            mView = GeogiftMakerFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.geogift_maker_fragment_container, (GeogiftMakerFragment)mView);
            transaction.commit();
        }

        mController = new FirebaseGeogiftMakerController(super.mCoupleUid, super.mUserUid);
        mPresenter = new GeogiftMakerPresenter(mView, mController, super.mUserUid);

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
    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.title_alter_dialog_back);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GeogiftMakerActivity.super.onBackPressed();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface dialog) {

            }
        });
        builder.show();
    }
}
