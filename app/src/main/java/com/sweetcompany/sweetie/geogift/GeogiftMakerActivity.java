package com.sweetcompany.sweetie.geogift;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseGeogiftMakerController;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by ghiro on 07/08/2017.
 */

public class GeogiftMakerActivity extends AppCompatActivity implements
                                                       GoogleApiClient.ConnectionCallbacks,
                                                       GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "GeogiftMakerActivity";

    // key for Intent extras
    public static final String GEOGIFT_TITLE = "GeogiftTitle";
    private GoogleApiClient googleApiClient;

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

        String userMail = Utility.getStringPreference(this, Utility.MAIL);
        String userUID = Utility.getStringPreference(this, Utility.USER_UID);
        String coupleUid = Utility.getStringPreference(this, Utility.COUPLE_UID);

        mController = new FirebaseGeogiftMakerController(coupleUid);
        mPresenter = new GeogiftMakerPresenter(mView, mController, userUID);

        //create GoogleApiClient
        createGoogleApi();
        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        googleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
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


    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    //Google ApiClient Connection Listeners
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Google ApiClient onConnected()");
        //getLastKnownLocation();
        // initialize GoogleMaps
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "Google ApiClient onConnectionSuspended()");
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "Google ApiClient onConnectionFailed()");
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // TODO adjust dialog messages
        builder.setTitle(R.string.title_alter_dialog_back);
        //builder.setMessage("");
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
        //super.onBackPressed();
    }
}
