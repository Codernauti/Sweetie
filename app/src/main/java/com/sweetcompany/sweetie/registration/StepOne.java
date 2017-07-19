package com.sweetcompany.sweetie.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;

// TODO: extract firebase dependencies
public class StepOne extends Fragment implements RegisterContract.LoginView,
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "StepOne";
    private static final int RC_SIGN_IN = 9001;

    private Context mContext;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    private RegisterContract.LoginPresenter mLoginPresenter;

    private SignInButton mRegisterGoogleButton;
    private ProgressBar mProgressBar;

    public static StepOne newInstance(Bundle bundle) {
        StepOne newFragment = new StepOne();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_step_one, container, false);
        mContext = getContext();

        // Assign fields
        mRegisterGoogleButton = (SignInButton) view.findViewById(R.id.register_google_button);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_register);
        mAuth = FirebaseAuth.getInstance();

        // Set click listeners
        mRegisterGoogleButton.setOnClickListener(this);

        // TODO: move this code into a Controller
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_google_button:
                setProgressBarVisible(true);
                signIn();
                break;
            default:
                return;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
                Log.e(TAG, String.valueOf(result));
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        //TODO: warning thread pool shared!
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            setProgressBarVisible(false);
                        }
                        else {
                            //save id token
                            // TODO: here or after into RegisterActivity?
                            Utility.saveStringPreference(mContext,Utility.USER_UID,task.getResult().getUser().getUid());
                            Utility.saveStringPreference(mContext,Utility.MAIL,task.getResult().getUser().getEmail());

                            mLoginPresenter.attachUserCheckListener(task.getResult().getUser().getUid());
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(getActivity(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setPresenter(RegisterContract.LoginPresenter loginPresenter) {
        mLoginPresenter = loginPresenter;
    }

    /*@Override
    public void notifyUserCheck(UserVM userVM) {
        if (userVM != null) {
            Utility.saveStringPreference(mContext, Utility.USERNAME, userVM.getUsername());
            Utility.saveStringPreference(mContext, Utility.PHONE_NUMBER, userVM.getPhone());
            Utility.saveStringPreference(mContext, Utility.GENDER, String.valueOf(userVM.isGender()));
            ((RegisterActivity) getActivity()).registrationCompleted();
        }
        else{
            // go to Step 2
            setProgressBarVisibile(false);
            StepTwo mFragment = new StepTwo();
            FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );
            mTransaction.addToBackStack("stepOne");
            mTransaction.replace(R.id.register_fragment_container,mFragment);
            mTransaction.commit();
            ((RegisterActivity) getActivity()).setPresenter(mFragment);
        }
    }*/

    @Override
    public void setProgressBarVisible(boolean b) {
        if(b){
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
