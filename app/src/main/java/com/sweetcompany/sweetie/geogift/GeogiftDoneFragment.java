package com.sweetcompany.sweetie.geogift;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.R;

/**
 * Created by ghiro on 17/08/2017.
 */

public class GeogiftDoneFragment extends Fragment {

    private static final String TAG = "GeogiftDoneFragment";

    private Toolbar mToolBar;

    private Context mContext;
    private String titleGeogift;

    public static GeogiftDoneFragment newInstance(Bundle bundle) {
        GeogiftDoneFragment newGeogiftDoneFragment = new GeogiftDoneFragment();
        newGeogiftDoneFragment.setArguments(bundle);

        return newGeogiftDoneFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.geogift_done_fragment, container, false);

        // TODO: is useless to set titleGeogift, Firebase update it also if it is offline
        titleGeogift = getArguments().getString(GeogiftDoneActivity.GEOGIFT_TITLE);
        Log.d(TAG, "from Intent GEOGIFT_TITLE: " + titleGeogift);

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.geogift_done_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleGeogift);


        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
