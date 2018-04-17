package com.codernauti.sweetie.registration;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.codernauti.sweetie.BaseActivity;
import com.codernauti.sweetie.MainActivity;
import com.codernauti.sweetie.UserMonitorService;
import com.codernauti.sweetie.chat.MessagesMonitorService;
import com.codernauti.sweetie.firebase.FirebaseRegisterController;
import com.codernauti.sweetie.firebase.FirebaseSettingsController;
import com.codernauti.sweetie.R;
import com.codernauti.sweetie.model.UserFB;
import com.codernauti.sweetie.utils.SharedPrefKeys;
import com.codernauti.sweetie.utils.Utility;
import com.codernauti.sweetie.pairing.PairingActivity;


public class RegisterActivity extends BaseActivity implements
        FirebaseRegisterController.Listener {

    private static final String TAG = "RegisterActivity";


    private SetInfoFragment mViewSetInfo;
    private SetUserImgFragment mViewSetImage;

    private FirebaseRegisterController mRegisterController;
    private FirebaseSettingsController mSettingsController;

    private String mUserUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        mViewSetInfo = null;
        // Class cast exception if SetInfoFragment is in register_fragment_container
                /*(LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.register_fragment_container);*/

        if (mViewSetInfo == null) {
            mViewSetInfo = SetInfoFragment.newInstance(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.register_fragment_container, mViewSetInfo)
                    .addToBackStack(SetInfoFragment.TAG)
                    .commit();
        }

        mUserUid = Utility.getStringPreference(this, SharedPrefKeys.USER_UID);

        mRegisterController = new FirebaseRegisterController();
        mRegisterController.setListener(this);

        mSettingsController = new FirebaseSettingsController(mUserUid);
    }


    // back management

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            Log.d(TAG, "back stack empty, user on SetInfo");

            new AlertDialog.Builder(this)
                    .setTitle("Cancel the registration?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RegisterActivity.super.signOutFromGoogleAPIClient();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
        else {
            Log.d(TAG, "popBackStack, user on SetImage");
            getSupportFragmentManager().popBackStack();
        }
    }


    // Fragment callbacks

    public void showNextStep() {
        hideSoftKeyboard();

        mViewSetImage = null; //(SetUserImgFragment) getSupportFragmentManager().findFragmentByTag(SetUserImgFragment.TAG);

        if (mViewSetImage == null) {
            mViewSetImage = SetUserImgFragment.newInstance(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    .addToBackStack(SetUserImgFragment.TAG)
                    .replace(R.id.register_fragment_container, mViewSetImage)
                    .commit();
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        View v = getCurrentFocus();
        if (v != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void registrationComplete() {
        // push user
        String username = Utility.getStringPreference(this, SharedPrefKeys.USERNAME);
        String email = Utility.getStringPreference(this, SharedPrefKeys.MAIL);
        String phoneNumber = Utility.getStringPreference(this, SharedPrefKeys.PHONE_NUMBER);
        boolean gender = Utility.getBooleanPreference(this, SharedPrefKeys.GENDER);

        UserFB user = new UserFB(username, email, phoneNumber, gender);
        user.setKey(mUserUid);

        mRegisterController.saveUserData(user);
    }


    // Controllers callback

    @Override
    public void onUserUploaded() {
        // user registered
        Utility.saveBooleanPreference(this, SharedPrefKeys.REGISTERED_USER, true);

        // push image
        String userImageUri = Utility.getStringPreference(this, SharedPrefKeys.USER_IMAGE_URI);
        mSettingsController.changeUserImage(Uri.parse(userImageUri));

        // start services
        startService(new Intent(this, UserMonitorService.class));
        startService(new Intent(this, MessagesMonitorService.class));

        // start PairingActivity
        Intent intent = new Intent(this, PairingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(PairingActivity.USER_FROM_REGISTER_KEY, true);

        TaskStackBuilder.create(this)
                .addParentStack(PairingActivity.class)   // start DashboardActivity
                .addNextIntent(intent)
                .startActivities();
    }

}