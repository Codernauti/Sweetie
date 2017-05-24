package com.sweetcompany.sweetie.Registration;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.sweetcompany.sweetie.Firebase.FirebaseController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;


public class StepThree extends Fragment implements View.OnClickListener {
    private final FirebaseController mFireBaseController = FirebaseController.getInstance();

    private static final int PICK_CONTACT = 1;
    private Button mForwardButton;
    private EditText mPhoneText;
    private ImageButton mContactsButton;
    private String mPhoneNumber;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step_three, container, false);
        // Assign fields
        mForwardButton = (Button) view.findViewById(R.id.fordward_button);
        mPhoneText = (EditText) view.findViewById(R.id.phone_request_input);
        mContactsButton = (ImageButton) view.findViewById(R.id.image_contacts_icon);
        // Set click listeners
        mForwardButton.setOnClickListener(this);

        mContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);

            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fordward_button:
                saveRequest();
                Toast.makeText(getActivity(), "Request successfully sent!",
                        Toast.LENGTH_SHORT).show();
                ((RegisterActivity) getActivity()).registrationCompleted();
                break;

            default:
                return;
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
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(phoneIndex);
            return phoneNo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveRequest(){
        RequestVM request = new RequestVM(Utility.getStringPreference(getContext(),"phoneNumber"),mPhoneNumber);
        mFireBaseController.getDatabase().getReference().child("requests").push().setValue(request);
    }
}
