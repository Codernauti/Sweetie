package com.sweetcompany.sweetie;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sweetcompany.sweetie.registration.RegisterActivity;

public class MainActivity extends AppCompatActivity{

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent( context, MainActivity.class );
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // user log out
            startActivity(new Intent(this, RegisterActivity.class));
        } else {
            // User logged, start Service UserMonitorService
            startService(new Intent(this, UserMonitorService.class));
            startActivity(new Intent(this, DashboardActivity.class));
            startService(new Intent(this, GeogiftMonitorService.class));
        }

        /*if (Utility.checkPreferencesSetted(getApplicationContext())) {
            // User logged, start Service UserMonitorService
            startService(new Intent(this, UserMonitorService.class));
            startActivity(new Intent(this, DashboardActivity.class));
        }
        else {
            startActivity(new Intent(this, RegisterActivity.class));
        }*/
    }
}
