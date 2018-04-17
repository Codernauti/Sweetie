package com.codernauti.sweetie.registration;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.codernauti.sweetie.R;
import com.codernauti.sweetie.utils.SharedPrefKeys;
import com.codernauti.sweetie.utils.Utility;


public class SetInfoFragment extends Fragment implements RegisterContract.RegisterView, View.OnClickListener {

    static final String TAG = "SetInfoFragment";

    private Button mForwardButton;
    private EditText mUsernameText;
    private EditText mPhoneText;
    private RadioButton mMaleRadio;
    private RadioButton mFemaleRadio;

    private Context mContext;

    private RegisterContract.RegisterPresenter mPresenter;

    public static SetInfoFragment newInstance(Bundle extras) {
        SetInfoFragment newFragment = new SetInfoFragment();
        newFragment.setArguments(extras);
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.register_step_two, container, false);
        mContext = getContext();

        // Assign fields
        mForwardButton = (Button) root.findViewById(R.id.pairing_next_button);
        mUsernameText = (EditText) root.findViewById(R.id.username_input);
        mPhoneText = (EditText) root.findViewById(R.id.phone_input);
        mMaleRadio = (RadioButton)root.findViewById(R.id.radio_button_male);
        mFemaleRadio = (RadioButton) root.findViewById(R.id.radio_button_famale);

        // populate field if already exist sp
        String username = Utility.getStringPreference(getContext(), SharedPrefKeys.USERNAME);
        String phone = Utility.getStringPreference(getContext(), SharedPrefKeys.PHONE_NUMBER);
        // true if male
        boolean gender = Utility.getBooleanPreference(getContext(), SharedPrefKeys.GENDER);

        if (!username.equals(SharedPrefKeys.DEFAULT_VALUE)) {
            mUsernameText.setText(username);
        }

        if (!phone.equals(SharedPrefKeys.DEFAULT_VALUE)) {
            mPhoneText.setText(phone);
        }

        mFemaleRadio.setChecked(gender);



        Toolbar toolbar = (Toolbar) root.findViewById(R.id.credentials_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = parentActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Set click listeners
        mForwardButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pairing_next_button:
                /*// get data of Auth
                String userUid = Utility.getStringPreference(mContext, SharedPrefKeys.USER_UID);
                String userEmail = Utility.getStringPreference(mContext, SharedPrefKeys.MAIL);*/

                String username = mUsernameText.getText().toString();
                String phoneNumber = mPhoneText.getText().toString();
                boolean gender = mMaleRadio.isChecked();

                if (username.isEmpty() && phoneNumber.isEmpty()) {
                    Toast.makeText(mContext, "Username and phone number can not be empty", Toast.LENGTH_SHORT).show();
                } else if (username.length() <= 0) {
                    Toast.makeText(mContext, "Username can not be empty", Toast.LENGTH_SHORT).show();
                } else if (phoneNumber.isEmpty()) {
                    Toast.makeText(mContext, "Phone number can not be empty", Toast.LENGTH_SHORT).show();
                } else if (phoneNumber.length() != 10){
                    Toast.makeText(getContext(), "A phone number must have 10 characters", Toast.LENGTH_SHORT).show();
                } else {
                    savePreferences(username, phoneNumber, gender);

                    ((RegisterActivity) getActivity()).showNextStep();
                    //mPresenter.saveUserData(userUid, userEmail, username, phoneNumber, gender);
                }
                break;
            default:
                break;
        }
    }

    // TODO: move this method in register activity?
    private void savePreferences(String username, String phoneNumber, boolean gender) {
        Utility.saveStringPreference(mContext, SharedPrefKeys.USERNAME, username);
        Utility.saveStringPreference(mContext, SharedPrefKeys.PHONE_NUMBER, phoneNumber);
        Utility.saveBooleanPreference(mContext, SharedPrefKeys.GENDER, gender);
    }


    @Override
    public void setPresenter(RegisterContract.RegisterPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNextScreen() {
        ((RegisterActivity) getActivity()).showNextStep();
    }
}
