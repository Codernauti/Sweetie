package com.sweetcompany.sweetie;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.sweetcompany.sweetie.Firebase.FirebaseController;
import com.sweetcompany.sweetie.Registration.RegisterActivity;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{

    private final FirebaseController mFireBaseController = FirebaseController.getInstance();

    public static final String ALREADY_REGISTED = "rip.tutorial";

    // Firebase instance variables
    private FirebaseUser mFirebaseUser;

    // Button
    private Button mRegisterButton;
    private Button mLoginButton;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //mFireBaseController.init();

        //se non si è mai registrato
        /* if (PreferenceManager.getDefaultSharedPreferences(this).getInt(ALREADY_REGISTED, 0) == 0) {
            startActivity(new Intent(this, RegisterActivity.class));
        }*/
        if (mFireBaseController.getFirebaseUser() != null)
        {
            startActivity(new Intent(this, DashboardActivity.class));
            //startActivity(new Intent(this, LoginActivity.class));
        } else if (mFireBaseController.getFirebaseUser() == null)
        {
            startActivity(new Intent(this, LoginActivity.class));
        }

        /*//check if user is logged in. If not go to the login page.
        // Initialize Firebase Auth
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            startActivity(new Intent(this, DashboardActivity.class));
        } else {
            // User is signed out
            //startActivity(new Intent(this, LoginActivity.class));
            // Split if User need to login or register TODO
        }

        mRegisterButton = (Button) findViewById (R.id.register_button);
        mLoginButton = (Button) findViewById (R.id.login_button);

        mRegisterButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);*/


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
        }

    }
}
