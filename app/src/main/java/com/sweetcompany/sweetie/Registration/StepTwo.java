package com.sweetcompany.sweetie.Registration;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.sweetcompany.sweetie.Firebase.FirebaseController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;

import java.util.List;


public class StepTwo extends Fragment implements RegisterContract.View, View.OnClickListener {

    private final FirebaseController mFireBaseController = FirebaseController.getInstance();

    private static final String TAG = "StepTwo";


    private Button mForwardButton;
    private EditText mUsernameText;
    private EditText mPhoneText;
    private RadioButton mRadio;
    private Context mContext;
    private RegisterContract.Presenter mPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step_two, container, false);
        // Assign fields
        mForwardButton = (Button) view.findViewById(R.id.pairing_send_button);
        mUsernameText = (EditText) view.findViewById(R.id.username_input);
        mPhoneText = (EditText) view.findViewById(R.id.phone_input);
        mRadio = (RadioButton)view.findViewById(R.id.radio_button_male);

        // Set click listeners
        mForwardButton.setOnClickListener(this);

        mContext = getContext();
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pairing_send_button:
                savePreferences();
                mPresenter.saveUserData(mContext);

                ((RegisterActivity) getActivity()).initAndOpenPairingFragment();

                /*PairingFragment nextFragment = new PairingFragment();
                FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );
                mTransaction.addToBackStack("stepTwo");
                mTransaction.replace(R.id.register_fragment_container, nextFragment);
                mTransaction.commit();
                ((RegisterActivity) getActivity()).setPresenter(nextFragment);*/

                break;
            default:
                return;
        }
    }

    private void savePreferences() {
        //TODO Check if not empty
        String mUsername = mUsernameText.getText().toString();
        String mPhoneNumber = mPhoneText.getText().toString();
        boolean mGender = mRadio.isChecked();
        Utility.saveStringPreference(mContext,Utility.USERNAME, mUsername);
        Utility.saveStringPreference(mContext,Utility.PHONE_NUMBER, mPhoneNumber);
        Utility.saveStringPreference(mContext,Utility.GENDER,String.valueOf(mGender));
    }


    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateRequest(List<PairingRequestVM> pairingRequestsVM) {}

    @Override
    public void notifyUser(UserVM usersVM) {}

    @Override
    public void notifyUserCheck(UserVM userVM) {}

}
