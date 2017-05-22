package com.sweetcompany.sweetie.Registration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sweetcompany.sweetie.Firebase.FirebaseController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;


public class StepTwo extends Fragment implements View.OnClickListener {

    private final FirebaseController mFireBaseController = FirebaseController.getInstance();

    private static final String TAG = "RegisterActivity";
    private static final int RC_SIGN_IN = 9001;

    private Button mForwardButton;
    private EditText mUsernameText;
    private EditText mPhoneText;
    private RadioButton mRadio;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_step_two, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Assign fields
        mForwardButton = (Button) view.findViewById(R.id.fordward_button);
        mUsernameText = (EditText) view.findViewById(R.id.username_input);
        mPhoneText = (EditText) view.findViewById(R.id.phone_input);
        mRadio = (RadioButton)view.findViewById(R.id.radio_button_male);

        // Set click listeners
        mForwardButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fordward_button:
                savePreferences();
                ((RegisterActivity) getActivity()).onPageSelected(RegisterPagerAdapter.STEP_THREE);
                //((RegisterActivity) getActivity()).registrationCompleted();
                break;
            default:
                return;
        }
    }

    private void savePreferences() {
        String mUsername = mUsernameText.getText().toString();
        String mPhoneNumber = mPhoneText.getText().toString();
        boolean mGender = mRadio.isChecked();
        if (Utility.saveStringPreference(getContext(),"username", mUsername) == false) {
            throwError();
        }
        if (Utility.saveStringPreference(getContext(),"phoneNumber", mPhoneNumber) == false) {
            throwError();
        }
        if (Utility.saveStringPreference(getContext(),"gender",String.valueOf(mGender)) == false) {
            throwError();
        }
    }

    private void throwError(){
        Toast.makeText(getActivity(), "Error saving preferences", Toast.LENGTH_SHORT).show();
    }
}
