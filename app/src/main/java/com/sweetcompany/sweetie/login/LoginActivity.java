package com.sweetcompany.sweetie.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sweetcompany.sweetie.MainActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseLoginController;
import com.sweetcompany.sweetie.model.UserFB;
import com.sweetcompany.sweetie.registration.RegisterActivity;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;


public class LoginActivity extends AppCompatActivity implements FirebaseLoginController.FbLoginControllerListener {

    private static final String TAG = "LoginActivity";

    private LoginFragment mView;
    private LoginPresenter mPresenter;
    private FirebaseLoginController mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        /*if (savedInstanceState == null) {
            savedInstanceState = getIntent().getExtras();
        }

        if (savedInstanceState != null) {
            mUserFromRegister = savedInstanceState.getBoolean(USER_FROM_REGISTER_KEY);
        }*/


        mView = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.pairing_fragment_container);

        if (mView == null) {
            mView = LoginFragment.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.login_fragment_container, mView);
            transaction.commit();
        }

        mController = new FirebaseLoginController();
        mPresenter = new LoginPresenter(mView, mController);

        // remember to remove that listener
        mController.addListener(mPresenter);
        mController.addListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.removeListener(this);
        mController.removeListener(mPresenter);
    }

    // Controller callback

    @Override
    public void onUserDownloadFinished(UserFB user) {
        if (user != null) {
            Log.d(TAG, "user already have an account");

            Utility.saveStringPreference(this, SharedPrefKeys.USERNAME, user.getUsername());
            Utility.saveStringPreference(this, SharedPrefKeys.PHONE_NUMBER, user.getPhone());
            Utility.saveBooleanPreference(this, SharedPrefKeys.GENDER, user.getGender());

            Utility.saveBooleanPreference(this, SharedPrefKeys.REGISTERED_USER, true);

            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            Log.d(TAG, "use == null, he need to register");

            mController.removeListener(this);
            mController.removeListener(mPresenter);

            startActivity(new Intent(this, RegisterActivity.class));
        }
    }
}
