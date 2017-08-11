package com.sweetcompany.sweetie.geogift;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.Utility;

import java.util.concurrent.Executor;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ghiro on 07/08/2017.
 */

public class GeogiftFragment  extends Fragment implements GeogiftContract.View {

    private static final String TAG = "GeogiftFragment";

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

    private FloatingActionButton mFabAddGeogift;

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

        messageIconButton = (ImageView) root.findViewById(R.id.item_message_geogift_button);
        messageIconButton.setClickable(true);
        photoIconButton = (ImageView) root.findViewById(R.id.item_photo_geogift_button);
        photoIconButton.setClickable(true);
        heartIconButton = (ImageView) root.findViewById(R.id.item_heart_geogift_button);
        heartIconButton.setClickable(true);
        messageSelector = (ImageView) root.findViewById(R.id.message_geogift_selector);
        messageSelector.setVisibility(View.GONE);
        photoSelector = (ImageView) root.findViewById(R.id.photo_geogift_selector);
        photoSelector.setVisibility(View.GONE);
        heartSelector = (ImageView) root.findViewById(R.id.heart_geogift_selector);
        heartSelector.setVisibility(View.GONE);

        /*imageThumb = (ImageView) root.findViewById(R.id.image_thumb_geogift);
        clearImageButton = (ImageView) root.findViewById(R.id.clear_image_geogift_button);

        mFabAddGeogift = (FloatingActionButton) root.findViewById(R.id.fab_new_geogift);
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
}
