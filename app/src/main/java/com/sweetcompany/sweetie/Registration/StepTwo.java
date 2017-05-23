package com.sweetcompany.sweetie.Registration;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sweetcompany.sweetie.Firebase.FirebaseController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;


public class StepTwo extends Fragment implements View.OnClickListener {

    private final FirebaseController mFireBaseController = FirebaseController.getInstance();

    private static final String TAG = "StepTwo";


    private Button mForwardButton;
    private EditText mUsernameText;
    private EditText mPhoneText;
    private RadioButton mRadio;
    private Context mContext;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step_two, container, false);
        // Assign fields
        mForwardButton = (Button) view.findViewById(R.id.fordward_button);
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
            case R.id.fordward_button:
                savePreferences();
                saveUserData();
                Fragment mFragment = new StepThree();
                FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );
                mTransaction.addToBackStack("stepTwo");
                mTransaction.replace(R.id.register_fragment_container,mFragment);
                mTransaction.commit();
                break;
            default:
                return;
        }
    }

    private void savePreferences() {
        String mUsername = mUsernameText.getText().toString();
        String mPhoneNumber = mPhoneText.getText().toString();
        boolean mGender = mRadio.isChecked();
        Utility.saveStringPreference(mContext,"username", mUsername);
        Utility.saveStringPreference(mContext,"phoneNumber", mPhoneNumber);
        Utility.saveStringPreference(mContext,"gender",String.valueOf(mGender));
    }

    private void saveUserData(){
        String token = Utility.getStringPreference(getContext(),"token");
        UserVM user= new UserVM(Utility.getStringPreference(getContext(),"username"),Utility.getStringPreference(getContext(),"phoneNumber"), Utility.getStringPreference(getContext(),"mail"),Boolean.valueOf(Utility.getStringPreference(getContext(),"gender")));
        mFireBaseController.getDatabase().getReference().child("users").child(token).setValue(user);
    }
}
