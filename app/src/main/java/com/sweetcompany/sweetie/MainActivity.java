package com.sweetcompany.sweetie;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sweetcompany.sweetie.registration.RegisterActivity;
import com.sweetcompany.sweetie.utils.Utility;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Log.d("Saved Mail User",Utility.getStringPreference(this,Utility.MAIL));

        if (Utility.checkPreferencesSetted(getApplicationContext())) {
            // User logged, start Service UserMonitorService
            startService(new Intent(this, UserMonitorService.class));
            startActivity(new Intent(this, DashboardActivity.class));
        }
        else {
            startActivity(new Intent(this, RegisterActivity.class));
        }


        //se non si è mai registrato

//        if (PreferenceManager.getDefaultSharedPreferences(this).getInt(ALREADY_REGISTED, 0) == 0) {
//            startActivity(new Intent(this, RegisterActivity.class));
//        }
        /*if (mFireBaseController.getFirebaseUser() != null)
        {
            startActivity(new Intent(this, DashboardActivity.class));
            //startActivity(new Intent(this, LoginActivity.class));
        }
        else if (mFireBaseController.getFirebaseUser() == null)
        {
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_button:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.login_button:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }*/
    }
}
