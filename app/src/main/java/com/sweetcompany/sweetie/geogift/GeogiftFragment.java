package com.sweetcompany.sweetie.geogift;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.sweetcompany.sweetie.R;

import static android.app.Activity.RESULT_OK;


/**
 * Created by ghiro on 07/08/2017.
 */

public class GeogiftFragment  extends Fragment implements GeogiftContract.View {

    private static final String TAG = "GeogiftFragment";

    private static final int PLACE_PICKER_REQUEST = 1;

    private Toolbar mToolBar;
    private TextView coordText;
    private Button pickPositionButton;

    private GeogiftContract.Presenter mPresenter;


    public static GeogiftFragment newInstance(Bundle bundle) {
        GeogiftFragment newGeogiftFragment = new GeogiftFragment();
        newGeogiftFragment.setArguments(bundle);

        return newGeogiftFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // root is a RelativeLayout
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.geogift_fragment, container, false);

        // TODO: is useless to set titleChat, Firebase update it also if it is offline
        String titleGeogift = getArguments().getString(GeogiftActivity.GEOGIFT_TITLE);
        String geogiftUid = getArguments().getString(GeogiftActivity.GEOGIFT_DATABASE_KEY);
        Log.d(TAG, "from Intent GEOGIFT_TITLE: " + titleGeogift);
        Log.d(TAG, "from Intent GEOGIFT_DATABASE_KEY: " + geogiftUid);

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.geogift_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleGeogift);

        pickPositionButton = (Button) root.findViewById(R.id.pick_location_button);

        pickPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPosition();
            }
        });

        return root;
    }

    public void pickPosition(){

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setPresenter(GeogiftContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
