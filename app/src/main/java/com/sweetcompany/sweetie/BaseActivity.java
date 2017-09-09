package com.sweetcompany.sweetie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sweetcompany.sweetie.couple.CoupleActivity;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by Eduard on 26-Aug-17.
 */

/**
 *  BaseActivity class for every activity of app, her responsibilities are:
 *  - Manage the login status
 *  - Manage the user relationship status
 *      if app in foreground onSharedPreferenceChange() is trigger
 *      if app in background onStart() check if couple_uid changed
 */
public class BaseActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String BASE_TAG = "BaseActivity";

    // utils fields for the correct working of all activity
    protected String mUserUid;
    protected String mUserEmail;
    protected String mCoupleUid;
    protected String mPartnerUid;

    protected GoogleApiClient mGoogleApiClient;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        /* Setup the Google API object to allow Google logins */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_ID))
                .requestEmail()
                .build();

        /* Setup the Google API object to allow Google+ logins */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Getting utils data for all activity
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mUserUid = sp.getString(SharedPrefKeys.USER_UID, SharedPrefKeys.DEFAULT_VALUE);
        mUserEmail = sp.getString(SharedPrefKeys.MAIL, SharedPrefKeys.DEFAULT_VALUE);
        mCoupleUid = sp.getString(SharedPrefKeys.COUPLE_UID, SharedPrefKeys.DEFAULT_VALUE);
        mPartnerUid = sp.getString(SharedPrefKeys.PARTNER_UID, SharedPrefKeys.DEFAULT_VALUE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(BASE_TAG, "onStart()");

        if (mUserUid.equals(SharedPrefKeys.DEFAULT_VALUE)) {
            signOutAutomatically();
        }

        boolean coupleUidChanged = Utility.getBooleanPreference(this, SharedPrefKeys.USER_RELATIONSHIP_STATUS_CHANGED);
        if (coupleUidChanged) {
            checkUserRelationshipStatus();
        }

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        // if (!(this instanceof RegisterActivity)) {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) { // user sign out
                    signOutAutomatically();
                    takeUserToLoginScreenOnUnAuth();
                }
            }
        };
        mFirebaseAuth.addAuthStateListener(mAuthListener);
        //}
    }

    private void signOutAutomatically() {
        Utility.clearSharedPreferences(BaseActivity.this);
        FirebaseAuth.getInstance().signOut();

        //Auth.GoogleSignInApi.signOut(mGoogleApiClient);   generate nullpointerex if mGoogleApiClient is not ready
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
    }

    private void checkUserRelationshipStatus() {
        // Reset coupleUidChanged
        Utility.saveBooleanPreference(this, SharedPrefKeys.USER_RELATIONSHIP_STATUS_CHANGED, false);

        int userRelationshipStatus = Utility.getUserRelationshipStatus(this,
                SharedPrefKeys.USER_RELATIONSHIP_STATUS);
        String partnerUsername = Utility.getStringPreference(this, SharedPrefKeys.PARTNER_USERNAME);
        String partnerImageUri = Utility.getStringPreference(this, SharedPrefKeys.PARTNER_IMAGE_URI);
        mCoupleUid = Utility.getStringPreference(this, SharedPrefKeys.COUPLE_UID);

        Log.d(BASE_TAG, "onSharedPreferenceChanged() - USER_RELATIONSHIP_STATUS = " + userRelationshipStatus);

        if (userRelationshipStatus == UserMonitorService.BREAK_SINGLE) {
            removePartnerPreferenceValues();
            startCoupleActivity(getString(R.string.break_up_notification) + partnerUsername, partnerImageUri, true);
        }
        else if (userRelationshipStatus == UserMonitorService.COUPLED) {
            startCoupleActivity(getString(R.string.couple_notification) + partnerUsername, partnerImageUri, false);
        }
    }

    private void removePartnerPreferenceValues() {
        Utility.removePreference(this, SharedPrefKeys.PARTNER_UID);
        Utility.removePreference(this, SharedPrefKeys.PARTNER_USERNAME);
        Utility.removePreference(this, SharedPrefKeys.FUTURE_PARTNER_PAIRING_REQUEST);
    }

    private void startCoupleActivity(String messageToShow, String uriImage, boolean coupleBreak) {
        // start activity and clean the Task (back stack of activity)
        Intent intent = new Intent(this, CoupleActivity.class);
        intent.putExtra(CoupleActivity.FIRST_MESSAGE_KEY, messageToShow);
        intent.putExtra(CoupleActivity.IMAGE_PARTNER_KEY, uriImage);
        intent.putExtra(CoupleActivity.BREAK_KEY, coupleBreak);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

    private void takeUserToLoginScreenOnUnAuth() {
        // TODO move user to MainActivity or RegisterActivity?
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(BASE_TAG, "onPause()");

        /* Cleanup the AuthStateListener */
        /*if (!(this instanceof RegisterActivity)) {*/
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
        /*}*/

        // remove shared preferences listener
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }


    // Google API listener

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(BASE_TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    // com_sweetcompany_sweetie_preferences.xml listener

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        if (key.equals(SharedPrefKeys.USER_RELATIONSHIP_STATUS)) {
            checkUserRelationshipStatus();
        } else if (key.equals(SharedPrefKeys.USER_UID)) {
            signOutAutomatically();
        }
    }

}
