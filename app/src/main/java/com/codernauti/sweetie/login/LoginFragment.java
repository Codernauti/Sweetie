package com.codernauti.sweetie.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.codernauti.sweetie.R;
import com.codernauti.sweetie.utils.SharedPrefKeys;
import com.codernauti.sweetie.utils.Utility;

// TODO: manage the GoogleApiClient into RegisterActivity
public class LoginFragment extends Fragment implements LoginContract.LoginView, View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    static final String TAG = "LoginFragment";

    private static final int RC_SIGN_IN = 9001;

    private Context mContext;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    private LoginContract.LoginPresenter mLoginPresenter;

    private SignInButton mRegisterGoogleButton;
    private ProgressBar mProgressBar;

    public static LoginFragment newInstance(Bundle bundle) {
        LoginFragment newFragment = new LoginFragment();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: move this code into a Controller
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_ID))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 16, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.login_fragment, container, false);
        mContext = getContext();

        // Assign fields
        mRegisterGoogleButton = (SignInButton) root.findViewById(R.id.register_with_google);
        mProgressBar = (ProgressBar) root.findViewById(R.id.register_progress_bar);
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.pairing_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = parentActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mRegisterGoogleButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_with_google:
                setProgressBarVisible(true);
                signIn();
                break;
            default:
                break;
        }
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            setProgressBarVisible(false);

            if (result.isSuccess()) {

                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }

            } else {
                Log.d(TAG, "Google Sign In failed.");
                Log.e(TAG, String.valueOf(result));
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        else if (FirebaseAuth.getInstance().getCurrentUser() != null){
                            //save id token
                            // TODO: here or after into RegisterActivity?
                            Utility.saveStringPreference(mContext, SharedPrefKeys.USER_UID,
                                    task.getResult().getUser().getUid());
                            Utility.saveStringPreference(mContext, SharedPrefKeys.MAIL,
                                    task.getResult().getUser().getEmail());

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
    public void setPresenter(LoginContract.LoginPresenter loginPresenter) {
        mLoginPresenter = loginPresenter;
    }

    @Override
    public void setProgressBarVisible(boolean visible) {
        if(visible){
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
