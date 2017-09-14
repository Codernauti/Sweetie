package com.sweetcompany.sweetie;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sweetcompany.sweetie.chat.MessagesMonitorService;
import com.sweetcompany.sweetie.firebase.FirebaseSettingsController;
import com.sweetcompany.sweetie.firebase.FirebaseUserController;
import com.sweetcompany.sweetie.geogift.GeogiftDoneActivity;
import com.sweetcompany.sweetie.login.LoginActivity;
import com.sweetcompany.sweetie.registration.RegisterActivity;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        boolean registerUser = Utility.getBooleanPreference(this, SharedPrefKeys.REGISTERED_USER);

        if (user != null && registerUser) {
            // user already logged and register
            checkOptions();

            if (Utility.getBooleanPreference(this, SharedPrefKeys.Options.GEOGIFT_ENABLED)) {
                startService(new Intent(this, GeogiftMonitorService.class));
            }

            startService(new Intent(this, MessagesMonitorService.class));
            startService(new Intent(this, UserMonitorService.class));
            startActivity(new Intent(this, DashboardActivity.class));
        }
        else {
            // user not authenticated OR he sign out (shared pref clear)
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }

    // TODO: extract method
    // method for checking the option status of the app and resolve if something goes wrong
    private void checkOptions() {

        // check ACCESS_FINE_LOCATION permission
        if ( !(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) ) {
            Utility.removePreference(this, SharedPrefKeys.Options.GEOGIFT_ENABLED);
        }

    }
}
