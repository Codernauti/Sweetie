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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;

import java.util.List;


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
        View view = inflater.inflate(R.layout.pairing_fragment, container, false);

        // Assign fields
        mCancelButton = (Button) view.findViewById(R.id.pairing_cancel_button);
        mSendButton = (Button) view.findViewById(R.id.pairing_send_button);
        mContactsButton = (Button) view.findViewById(R.id.pairing_contacts_icon);
        mPhoneInputText = (EditText) view.findViewById(R.id.pairing_phone_request_input);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pairing_progress_bar);

        // Set click listeners
        mCancelButton.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
        mContactsButton.setOnClickListener(this);

        return view;
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
            case R.id.pairing_send_button:
                String partnerPhone = mPhoneInputText.getText().toString();
                mPresenter.sendPairingRequest(partnerPhone);
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
        // TODO: go to MainActivity for new check
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void setPresenter(PairingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}
