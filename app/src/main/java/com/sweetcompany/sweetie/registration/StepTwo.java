package com.sweetcompany.sweetie.registration;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;


public class StepTwo extends Fragment implements RegisterContract.RegisterView, View.OnClickListener {

    private static final String TAG = "StepTwo";

    private Button mForwardButton;
    private EditText mUsernameText;
    private EditText mPhoneText;
    private RadioButton mRadio;
    private Context mContext;

    private RegisterContract.RegisterPresenter mPresenter;

    public static StepTwo newInstance(Bundle extras) {
        StepTwo newFragment = new StepTwo();
        newFragment.setArguments(extras);
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step_two, container, false);
        mContext = getContext();

        // Assign fields
        mForwardButton = (Button) view.findViewById(R.id.pairing_send_button);
        mUsernameText = (EditText) view.findViewById(R.id.username_input);
        mPhoneText = (EditText) view.findViewById(R.id.phone_input);
        mRadio = (RadioButton)view.findViewById(R.id.radio_button_male);

        // Set click listeners
        mForwardButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pairing_send_button:
                // get data of Auth
                String userUid = Utility.getStringPreference(mContext, SharedPrefKeys.USER_UID);
                String userEmail = Utility.getStringPreference(mContext, SharedPrefKeys.MAIL);

                // get user's input
                //TODO Check if not empty
                String username = mUsernameText.getText().toString();
                String phoneNumber = mPhoneText.getText().toString();
                boolean gender = mRadio.isChecked();

                savePreferences(username, phoneNumber, gender);
                mPresenter.saveUserData(userUid, userEmail, username, phoneNumber, gender);

                break;
            default:
                break;
        }
    }

    // TODO: move this method in register activity?
    private void savePreferences(String username, String phoneNumber, boolean gender) {
        Utility.saveStringPreference(mContext, SharedPrefKeys.USERNAME, username);
        Utility.saveStringPreference(mContext, SharedPrefKeys.PHONE_NUMBER, phoneNumber);
        Utility.saveStringPreference(mContext, SharedPrefKeys.GENDER,String.valueOf(gender));
    }


    @Override
    public void setPresenter(RegisterContract.RegisterPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void startPairingActivity() {
        ((RegisterActivity) getActivity()).initServiceAndOpenPairingScreen();
    }
}
