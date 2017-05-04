package com.sweetcompany.sweetie.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sweetcompany.sweetie.MainActivity;
import com.sweetcompany.sweetie.R;

import java.util.concurrent.Executor;


public class StepTwo extends Fragment implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private static final int RC_SIGN_IN = 9001;

    private Button mForwardButton;

    public static StepOne newInstance() {
        StepOne stepOne = new StepOne();
        Bundle args = new Bundle();
        stepOne.setArguments(args);
        return stepOne;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_step_two, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Assign fields
        mForwardButton = (Button) view.findViewById(R.id.fordward_button);
        // Set click listeners
        mForwardButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fordward_button:
                ((RegisterActivity) getActivity()).registrationCompleted();
                break;
            default:
                return;
        }
    }


}
