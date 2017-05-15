package com.sweetcompany.sweetie.Registration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.sweetcompany.sweetie.R;


public class StepThree extends Fragment implements View.OnClickListener {

    private Button mForwardButton;
    private ImageButton mContactsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_step_three, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Assign fields
        mForwardButton = (Button) view.findViewById(R.id.fordward_button);
        mContactsButton = (ImageButton) view.findViewById(R.id.image_contacts_icon);
        // Set click listeners
        mForwardButton.setOnClickListener(this);

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
}
