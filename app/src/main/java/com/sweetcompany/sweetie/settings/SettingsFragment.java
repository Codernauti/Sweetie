package com.sweetcompany.sweetie.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sweetcompany.sweetie.R;

/**
 * Created by Eduard on 28-Aug-17.
 */

public class SettingsFragment extends Fragment implements SettingsContract.View, View.OnClickListener {

    private static final String TAG = "SettingsFragment";

    private SettingsContract.Presenter mPresenter;

    private ImageButton mChangeImageButton;
    private ImageView mImageView;
    private TextView mUsernameTextView;
    private TextView mEmailTextView;
    private TextView mGenderTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.settings_fragment, container, false);

        mImageView = (ImageView) root.findViewById(R.id.settings_image);
        mChangeImageButton = (ImageButton) root.findViewById(R.id.settings_change_image_button);
        mUsernameTextView = (TextView) root.findViewById(R.id.settings_username);
        mEmailTextView = (TextView) root.findViewById(R.id.settings_email);
        mGenderTextView = (TextView) root.findViewById(R.id.settings_gender);

        mChangeImageButton.setOnClickListener(this);

        return root;
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateUserInfo(String userImageUri, String username, String email, boolean gender) {
        Glide.with(getActivity())
                .load(userImageUri)
                .dontAnimate()
                .into(mImageView);

        mUsernameTextView.setText(username);
        mEmailTextView.setText(email);
        mGenderTextView.setText(gender ? getString(R.string.gender_male) : getString(R.string.gender_female));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_change_image_button:
                // open imagePicker
                Toast.makeText(getActivity(), "Feature not yet implemented", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
