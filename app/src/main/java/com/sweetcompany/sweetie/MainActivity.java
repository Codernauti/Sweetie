package com.sweetcompany.sweetie;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sweetcompany.sweetie.Registration.RegisterActivity;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER =  2;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    // Button
    private Button mRegisterButton;
    private Button mLoginButton;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        //check if user is logged in. If not go to the login page.
        // Initialize Firebase Auth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            startActivity(new Intent(this, DashboardActivity.class));
        } else {
            // User is signed out
            startActivity(new Intent(this, LoginActivity.class));
            // Split if User need to login or register TODO
        }

        mRegisterButton = (Button) findViewById (R.id.register_button);
        mLoginButton = (Button) findViewById (R.id.login_button);

        mRegisterButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);


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
