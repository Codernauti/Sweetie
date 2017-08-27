package com.sweetcompany.sweetie.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sweetcompany.sweetie.UserMonitorService;
import com.sweetcompany.sweetie.firebase.FirebaseLoginController;
import com.sweetcompany.sweetie.firebase.FirebaseRegisterController;
import com.sweetcompany.sweetie.model.UserFB;
import com.sweetcompany.sweetie.MainActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;
import com.sweetcompany.sweetie.pairing.PairingActivity;

// TODO: extend BaseActivity???
public class RegisterActivity extends AppCompatActivity
        implements FirebaseLoginController.FbLoginControllerListener {

    private StepOne mViewOne;
    private LoginPresenter mLoginPresenter;
    private FirebaseLoginController mLoginController;

    // Lazy initialization
    private StepTwo mViewTwo;
    private RegisterPresenter mRegisterPresenter;
    private FirebaseRegisterController mRegisterController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        mViewOne = null;
        // Class cast exception if StepTwo is in register_fragment_container
                /*(StepOne) getSupportFragmentManager()
                .findFragmentById(R.id.register_fragment_container);*/

        if (mViewOne == null) {
            mViewOne = StepOne.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.register_fragment_container, mViewOne);
            transaction.commit();
        }

        mLoginController = new FirebaseLoginController();
        mLoginPresenter = new LoginPresenter(mViewOne, mLoginController);

        // remember to remove that listener
        mLoginController.addListener(mLoginPresenter);
        mLoginController.addListener(this);
    }

    public void initServiceAndOpenPairingScreen() {
        startService(new Intent(this, UserMonitorService.class));
        startActivity(new Intent(this, PairingActivity.class));
    }

    @Override
    public void onUserDownloadFinished(UserFB user) {
        if (user != null) {
            Utility.saveStringPreference(this, SharedPrefKeys.USERNAME, user.getUsername());
            Utility.saveStringPreference(this, SharedPrefKeys.PHONE_NUMBER, user.getPhone());
            Utility.saveStringPreference(this, SharedPrefKeys.GENDER, String.valueOf(user.isGender()));
            startActivity(new Intent(this, MainActivity.class));
        }
        else {  // user == null, he need to sign in
            // Initialization of fragment StepTwo

            mViewTwo = null;
            // Class Cast exteption if StepOne is on register_fragment_container.
                    /*(StepTwo) getSupportFragmentManager()
                    .findFragmentById(R.id.register_fragment_container);*/

            if (mViewTwo == null) {
                mViewTwo = StepTwo.newInstance(getIntent().getExtras());
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .addToBackStack("stepOne")
                        .replace(R.id.register_fragment_container, mViewTwo);
                transaction.commit();
            }
            mRegisterController = new FirebaseRegisterController();
            mRegisterPresenter = new RegisterPresenter(mViewTwo, mRegisterController);

            mRegisterController.addListener(mRegisterPresenter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // remove all listeners from firebase to avoid memory leak
        mLoginController.removeListener(this);
        mLoginController.removeListener(mLoginPresenter);
    }
}