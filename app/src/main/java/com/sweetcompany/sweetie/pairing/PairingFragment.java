package com.sweetcompany.sweetie.pairing;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.R;


public class PairingFragment extends Fragment implements PairingContract.View,
        View.OnClickListener {

    private static final int PICK_CONTACT = 1;

    private Button mCancelButton;
    private Button mSendButton;
    private Button mContactsButton;
    private EditText mPhoneInputText;
    private ProgressBar mProgressBar;

    private PairingContract.Presenter mPresenter;

    public static PairingFragment newInstance(Bundle bundle) {
        PairingFragment newPairingFragment = new PairingFragment();
        newPairingFragment.setArguments(bundle);

        return newPairingFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pairing_fragment, container, false);

        // Assign fields
        mCancelButton = (Button) root.findViewById(R.id.pairing_cancel_button);
        mSendButton = (Button) root.findViewById(R.id.pairing_next_button);
        mContactsButton = (Button) root.findViewById(R.id.pairing_contacts_icon);
        mPhoneInputText = (EditText) root.findViewById(R.id.pairing_phone_request_input);
        mProgressBar = (ProgressBar) root.findViewById(R.id.pairing_progress_bar);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.pairing_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = parentActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Set click listeners
        mCancelButton.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
        mContactsButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    String partnerPhoneNumber = contactPicked(data);
                    partnerPhoneNumber = partnerPhoneNumber.replaceAll("^00\\d{2}|\\+\\d{2}|\\s|\\-","");
                    mPhoneInputText.setText(partnerPhoneNumber);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pairing_cancel_button:
                // TODO: manage two case, from Dashboard and from Registration
                mPhoneInputText.setText(null);
                getActivity().onBackPressed();
                break;
            case R.id.pairing_next_button:
                String partnerPhone = mPhoneInputText.getText().toString();
                if (partnerPhone.length() == 10) {
                    mPresenter.sendPairingRequest(partnerPhone);
                } else {
                    Toast.makeText(getContext(), "A phone number must have 10 characters", Toast.LENGTH_LONG).show();
                }
                break;
            case  R.id.pairing_contacts_icon:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoadingProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mCancelButton.setVisibility(View.INVISIBLE);
        mSendButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoadingProgress() {
        mProgressBar.setVisibility(View.GONE);
        mCancelButton.setVisibility(View.VISIBLE);
        mSendButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void startDashboardActivity() {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void setPresenter(PairingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
