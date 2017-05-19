package com.sweetcompany.sweetie.Registration;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
        mPhoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //TODO aggiungere controllo se il numero è gia presente nel db
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fordward_button:
                writeDB(v);
                ((RegisterActivity) getActivity()).onPageSelected(RegisterPagerAdapter.STEP_THREE);
                //((RegisterActivity) getActivity()).registrationCompleted();
                break;
            default:
                return;
        }
    }

    private void writeDB(View v){
        String userId = getIdToken();
        String mUsername = mUsernameText.getText().toString();
        String mPhone = mPhoneText.getText().toString();
        boolean mGender = mRadio.isChecked();
        User user = new User(mUsername,mPhone,mGender);
        mFireBaseController.getDatabaseUserReferences().child(userId).setValue(user);
    }

    private String getIdToken(){
        SharedPreferences settings = this.getActivity().getSharedPreferences("token", 0);
        Log.d("Recupero uid",settings.getString("id","error"));
        return settings.getString("id","error");

    }

}
