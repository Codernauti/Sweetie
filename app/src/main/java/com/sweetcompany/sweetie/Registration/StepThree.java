package com.sweetcompany.sweetie.Registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sweetcompany.sweetie.Firebase.FirebaseController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;

import java.util.List;


public class StepThree extends Fragment implements RegisterContract.View, View.OnClickListener {

    private static final int PICK_CONTACT = 1;

    Context mContext;
    private Button mForwardButton, mAcceptButton, mDeclineButton, mContactsButton;
    private LinearLayout mLinearLayout;
    private EditText mPhoneText;
    private TextView mPhoneView, mUsernameView;
    private String mPersonalPhoneNumber;
    private String mPhoneNumber;
    private String mKeyPairingRequest;
    private RegisterContract.Presenter mPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step_three, container, false);


        // Assign fields
        mContext = getContext();
        mLinearLayout = (LinearLayout) view.findViewById(R.id.pairing_request_layout);
        mForwardButton = (Button) view.findViewById(R.id.fordward_button);
        mPhoneText = (EditText) view.findViewById(R.id.phone_request_input);
        mContactsButton = (Button) view.findViewById(R.id.image_contacts_icon);
        mPhoneView = (TextView) view.findViewById(R.id.phone_text_view);
        mUsernameView = (TextView) view.findViewById(R.id.username_text_view);
        mAcceptButton = (Button) view.findViewById(R.id.accept_button);
        mDeclineButton = (Button) view.findViewById(R.id.decline_button);
        mPersonalPhoneNumber = Utility.getStringPreference(mContext,Utility.PHONE_NUMBER);

        mLinearLayout.setVisibility(View.GONE);

        // Set click listeners
        mForwardButton.setOnClickListener(this);
        mDeclineButton.setOnClickListener(this);
        mAcceptButton.setOnClickListener(this);
        mContactsButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.pause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fordward_button:
                mPresenter.savePairingRequest(Utility.getStringPreference(mContext,Utility.PHONE_NUMBER),
                        mPhoneText.getText().toString());
                Toast.makeText(getActivity(), "Request successfully sent!", Toast.LENGTH_SHORT).show();
                ((RegisterActivity) getActivity()).registrationCompleted();
                break;
            case  R.id.image_contacts_icon:
                Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                break;
            case R.id.decline_button:
                mPresenter.deletePairingRequest(mKeyPairingRequest);
                mLinearLayout.setVisibility(View.GONE);
                break;
            case R.id.accept_button:
                mPresenter.saveCoupleData(Utility.getStringPreference(mContext,Utility.TOKEN),
                                    Utility.getStringPreference(mContext,Utility.TOKEN_PARTNER));
                mPresenter.deletePairingRequest(mKeyPairingRequest);
                mLinearLayout.setVisibility(View.GONE);
                ((RegisterActivity) getActivity()).registrationCompleted();
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    mPhoneNumber = contactPicked(data);
                    mPhoneNumber = mPhoneNumber.replaceAll("^00\\d{2}|\\+\\d{2}|\\s|\\-","");
                    mPhoneText.setText(mPhoneNumber);
                }
                break;
        }
    }

    private String contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            Uri uri = data.getData();
            cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int  phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(phoneIndex);
            return phoneNo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateRequest(List<PairingRequestVM> pairingRequestsVM){
        if(pairingRequestsVM.size() != 0) {
            for (PairingRequestVM rqstVM : pairingRequestsVM) {
                if (rqstVM.getReceiverNumber().equals(mPersonalPhoneNumber)) {
                    mKeyPairingRequest = rqstVM.getKey();
                    mPresenter.attachUserDataListener("phone",rqstVM.getSenderNumber());
                }
            }
        }
    }

    @Override
    public void notifyUser(UserVM userVM) {
        if(userVM != null) {
            mPhoneView.setText(userVM.getPhone());
            mUsernameView.setText(userVM.getUsername());
            mLinearLayout.setVisibility(View.VISIBLE);
            Utility.saveStringPreference(mContext, Utility.TOKEN_PARTNER,userVM.getKey());
        }
    }

    @Override
    public void notifyUserCheck(UserVM userVM) {}


}
