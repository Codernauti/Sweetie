package com.sweetcompany.sweetie.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sweetcompany.sweetie.UserMonitorService;
import com.sweetcompany.sweetie.firebase.FirebaseLoginController;
import com.sweetcompany.sweetie.firebase.FirebaseRegisterController;
import com.sweetcompany.sweetie.firebase.FirebaseSettingsController;
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

    private StepSetUserImage mViewThree;
    private SetUserImagePresenter mSetUserImagePresenter;
    private FirebaseSettingsController mSettingsController;

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

        Intent intent = new Intent(this, PairingActivity.class);
        intent.putExtra(PairingActivity.USER_FROM_REGISTER_KEY, true);
        startActivity(intent);
    }

    @Override
    public void onUserDownloadFinished(UserFB user) {
        if (user != null) {
            Utility.saveStringPreference(this, SharedPrefKeys.USERNAME, user.getUsername());
            Utility.saveStringPreference(this, SharedPrefKeys.PHONE_NUMBER, user.getPhone());
            Utility.saveStringPreference(this, SharedPrefKeys.GENDER, String.valueOf(user.getGender()));
            startActivity(new Intent(this, MainActivity.class));
        }
        else {  // user == null, he need to sign in

            mLoginController.removeListener(this);
            mLoginController.removeListener(mLoginPresenter);

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
                        .addToBackStack(StepOne.TAG)
                        .replace(R.id.register_fragment_container, mViewTwo);
                transaction.commit();
            }
            mRegisterController = new FirebaseRegisterController();
            mRegisterPresenter = new RegisterPresenter(mViewTwo, mRegisterController);

            mRegisterController.setListener(mRegisterPresenter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // remove all listeners from firebase to avoid memory leak
        mLoginController.removeListener(this);
        mLoginController.removeListener(mLoginPresenter);

        if (mSettingsController != null) {
            mSettingsController.detachListener();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showStepThree() {
        mRegisterController.setListener(null);

        //

        mViewThree = null;

        if (mViewThree == null) {
            mViewThree = StepSetUserImage.newInstance(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );

            transaction.addToBackStack(StepSetUserImage.TAG);
            transaction.replace(R.id.register_fragment_container, mViewThree);
            transaction.commit();
        }

        String userUid = Utility.getStringPreference(this, SharedPrefKeys.USER_UID);

        mSettingsController = new FirebaseSettingsController(userUid);
        mSetUserImagePresenter = new SetUserImagePresenter(mViewThree, mSettingsController);

        mSettingsController.attachListener();
    }
}