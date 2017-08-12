package com.sweetcompany.sweetie.geogift;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.GeoUtils;

/**
 * Created by ghiro on 07/08/2017.
 */

public class GeogiftFragment extends Fragment implements GeogiftContract.View,
                                                         View.OnClickListener
                                                         {

    private static final String TAG = "GeogiftFragment";

    public static final int REQ_PERMISSION_UPDATE = 4001;

    private static final int MESSAGE_SELECTION = 0;
    private static final int PHOTO_SELECTION = 1;
    private static final int HEART_SELECTION = 2;

    private Toolbar mToolBar;
    //location picker topbar
    private ImageView locationPickerIcon;
    private TextView locationPickerText;
    //item selector bar
    private ImageView messageIconButton;
    private ImageView photoIconButton;
    private ImageView heartIconButton;
    private ImageView messageSelector;
    private ImageView photoSelector;
    private ImageView heartSelector;
    //image selector container
    private ImageView imageThumb;
    private ImageView clearImageButton;
    //fabButton
    private FloatingActionButton mFabAddGeogift;

    private Context mContext;



    public static GeogiftFragment newInstance(Bundle bundle) {
        GeogiftFragment newGeogiftFragment = new GeogiftFragment();
        newGeogiftFragment.setArguments(bundle);

        return newGeogiftFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.geogift_fragment, container, false);

        // TODO: is useless to set titleGeogift, Firebase update it also if it is offline
        String titleGeogift = getArguments().getString(GeogiftActivityTest.GEOGIFT_TITLE);
        String geogiftUid = getArguments().getString(GeogiftActivityTest.GEOGIFT_DATABASE_KEY);
        Log.d(TAG, "from Intent GEOGIFT_TITLE: " + titleGeogift);
        Log.d(TAG, "from Intent GEOGIFT_DATABASE_KEY: " + geogiftUid);

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.geogift_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleGeogift);

        locationPickerIcon = (ImageView) root.findViewById(R.id.geogift_icon_topbar);
        locationPickerIcon.setOnClickListener(this);
        locationPickerText = (TextView) root.findViewById(R.id.geogift_textview_topbar);
        locationPickerText.setOnClickListener(this);


        messageIconButton = (ImageView) root.findViewById(R.id.item_message_geogift_button);
        messageIconButton.setOnClickListener(this);
        photoIconButton = (ImageView) root.findViewById(R.id.item_photo_geogift_button);
        photoIconButton.setOnClickListener(this);
        heartIconButton = (ImageView) root.findViewById(R.id.item_heart_geogift_button);
        heartIconButton.setOnClickListener(this);
        messageSelector = (ImageView) root.findViewById(R.id.message_geogift_selector);
        messageSelector.setVisibility(View.GONE);
        photoSelector = (ImageView) root.findViewById(R.id.photo_geogift_selector);
        photoSelector.setVisibility(View.GONE);
        heartSelector = (ImageView) root.findViewById(R.id.heart_geogift_selector);
        heartSelector.setVisibility(View.GONE);

        imageThumb = (ImageView) root.findViewById(R.id.image_thumb_geogift);
        imageThumb.setVisibility(View.GONE);
        imageThumb.setOnClickListener(this);
        clearImageButton = (ImageView) root.findViewById(R.id.clear_image_geogift_button);
        clearImageButton.setVisibility(View.GONE);
        clearImageButton.setOnClickListener(this);

        /*mFabAddGeogift = (FloatingActionButton) root.findViewById(R.id.fab_new_geogift);
        mFabAddGeogift.setClickable(false);*/

        /*// Add listener
        mFabAddGeogift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

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

    @Override
    public void setPresenter(GeogiftContract.Presenter presenter) {

    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.geogift_icon_topbar:
            case R.id.geogift_textview_topbar:
                pickPosition();
                break;
            case R.id.item_message_geogift_button:
                switchContainerGift(MESSAGE_SELECTION);
                break;
            case R.id.item_photo_geogift_button:
                switchContainerGift(PHOTO_SELECTION);
                break;
            case R.id.item_heart_geogift_button:
                switchContainerGift(HEART_SELECTION);
                break;

            default:
                break;
        }
    }

    public void switchContainerGift(int item){
        switch ( item ){
            case MESSAGE_SELECTION:
                messageSelector.setVisibility(View.VISIBLE);
                photoSelector.setVisibility(View.GONE);
                heartSelector.setVisibility(View.GONE);

                imageThumb.setVisibility(View.GONE);
                clearImageButton.setVisibility(View.GONE);
                break;
            case PHOTO_SELECTION:
                messageSelector.setVisibility(View.GONE);
                photoSelector.setVisibility(View.VISIBLE);
                heartSelector.setVisibility(View.GONE);

                imageThumb.setVisibility(View.VISIBLE);
                clearImageButton.setVisibility(View.VISIBLE);
                break;
            case HEART_SELECTION:
                messageSelector.setVisibility(View.GONE);
                photoSelector.setVisibility(View.GONE);
                heartSelector.setVisibility(View.VISIBLE);

                imageThumb.setVisibility(View.GONE);
                clearImageButton.setVisibility(View.GONE);
                break;
        }
    }

    public void pickPosition(){
        if( GeoUtils.checkPermissionAccessFineLocation(mContext) ){

        }
        else{
            askPermissionAccessFineLocation();
        }
    }

    public void askPermissionAccessFineLocation() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION_UPDATE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION_UPDATE:
            {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    pickPosition();
                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
    }

 }
