package com.sweetcompany.sweetie.couple;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.pairing.PairingActivity;
import com.sweetcompany.sweetie.utils.Utility;

/**
 * Created by Eduard on 20-Jul-17.
 */

public class CoupleDetailsFragment extends Fragment implements CoupleDetailsContract.View,
        View.OnClickListener {

    private Button mFindPartnerButton;
    private Button mBreakButton;

    private CoupleDetailsContract.Presenter mPresenter;

    public static CoupleDetailsFragment newInstance() {
        return new CoupleDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.couple_details_fragment, container, false);

        mFindPartnerButton = (Button) root.findViewById(R.id.couple_details_find_partner_button);
        mBreakButton = (Button) root.findViewById(R.id.couple_details_break_button);

        if (Utility.getStringPreference(getContext(), Utility.COUPLE_UID).equals(Utility.DEFAULT_VALUE)) {
            mBreakButton.setVisibility(View.GONE);
        } else {
            mFindPartnerButton.setVisibility(View.GONE);
        }

        mFindPartnerButton.setOnClickListener(this);
        mBreakButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void setPresenter(CoupleDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.couple_details_break_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage("")
                        .setTitle("Are you sure?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.deleteCouple();
                            }
                        })
                        .setNegativeButton("Back", null)
                        .create()
                        .show();
                break;
            case R.id.couple_details_find_partner_button:
                startActivity(new Intent(getActivity(), PairingActivity.class));
                break;
            default:
                break;
        }
    }
}
