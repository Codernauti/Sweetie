package com.sweetcompany.sweetie.Registration;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;

import org.w3c.dom.Text;

import static android.app.Activity.RESULT_OK;


public class StepThree extends Fragment implements View.OnClickListener {

    private static final int PICK_CONTACT = 1;
    private Button mForwardButton;
    private EditText mPhoneText;
    private ImageButton mContactsButton;
    private String mPhoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_step_three, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fordward_button:

                break;
            case R.id.image_contacts_icon:

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
}
