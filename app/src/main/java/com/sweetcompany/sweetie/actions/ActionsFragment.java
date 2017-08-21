package com.sweetcompany.sweetie.actions;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.sweetcompany.sweetie.IPageChanger;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.geogift.GeoItem;
import com.sweetcompany.sweetie.geogift.GeofenceTrasitionService;
import com.sweetcompany.sweetie.utils.GeoUtils;

import java.util.ArrayList;
import java.util.List;

public class ActionsFragment extends Fragment implements ActionsContract.View, ResultCallback<Status>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ActionsFragment";

    private static final long GEO_DURATION = 10 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 100.0f; // in meters
    private static final int GEOFENCE_REQ_CODE = 4005;
    private static final int DWELL = 1000;


    private ActionsAdapter mActionAdapter;
    private RecyclerView mActionsListView;

    private FloatingActionButton mFabNewAction;
    private FloatingActionButton mFabNewChatAction;
    private FloatingActionButton mFabNewGalleryAction;
    private FloatingActionButton mFabNewToDoListAction;
    private FloatingActionButton mFabNewGeogiftAction;
    private Animation fab_small_open,fab_small_close, fab_open, fab_close, rotate_forward,rotate_backward;
    private boolean mIsFabOpen = false;

    private FrameLayout mFrameBackground;

    private static GoogleApiClient googleApiClient;
    private static PendingIntent geoFencePendingIntent;
    public ArrayList<Geofence> mGeofenceList;

    private ActionsContract.Presenter mPresenter;

    private Context mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionAdapter = new ActionsAdapter();
        mContext = getContext();

        //set animations
        fab_small_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_actions_open);
        fab_small_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_actions_close);
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(mContext, R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(mContext ,R.anim.rotate_backward);

    }

    @Override
    public void setPresenter(ActionsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.actions_fragment, container, false);

        mActionsListView = (RecyclerView) root.findViewById(R.id.actions_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true); //
        layoutManager.setStackFromEnd(true);  // ascendant order
        mActionsListView.setLayoutManager(layoutManager);
        mActionsListView.setAdapter(mActionAdapter);

        mFrameBackground = (FrameLayout) root.findViewById(R.id.frame_background);
        mFrameBackground.setAlpha(0f);
        mFrameBackground.setClickable(false);
        mFrameBackground.setVisibility(View.INVISIBLE);

        mFrameBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show others action fab
                if(mIsFabOpen){
                    animateFAB();
                }
            }
        });

        mFabNewAction = (FloatingActionButton) root.findViewById(R.id.fab_new_action);

        mFabNewChatAction = (FloatingActionButton) root.findViewById(R.id.fab_new_chat);
        mFabNewChatAction.setClickable(false);

        mFabNewGalleryAction = (FloatingActionButton) root.findViewById(R.id.fab_new_photo);
        mFabNewGalleryAction.setClickable(false);

        mFabNewToDoListAction = (FloatingActionButton) root.findViewById(R.id.fab_new_todolist);
        mFabNewToDoListAction.setClickable(false);

        mFabNewGeogiftAction = (FloatingActionButton) root.findViewById(R.id.fab_new_geogift);
        mFabNewGeogiftAction.setClickable(false);

        // Add listener
        mFabNewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show others action fab
                animateFAB();
            }
        });

        mFabNewChatAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide others action fab
                animateFAB();
                ActionNewChatFragment dialogFragment = ActionNewChatFragment.newInstance();
                dialogFragment.setPresenter(mPresenter);
                dialogFragment.show(getActivity().getFragmentManager(), ActionNewChatFragment.TAG);
            }
        });

        mFabNewGalleryAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
                ActionNewGalleryFragment dialogFragment = ActionNewGalleryFragment.newInstance();
                dialogFragment.setPresenter(mPresenter);
                dialogFragment.show(getActivity().getFragmentManager(), ActionNewGalleryFragment.TAG);
            }
        });

        mFabNewToDoListAction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                animateFAB();
                ActionNewToDoListFragment dialogFragment = ActionNewToDoListFragment.newInstance();
                dialogFragment.setPresenter(mPresenter);
                dialogFragment.show(getActivity().getFragmentManager(),ActionNewToDoListFragment.TAG);
            }
        });

        mFabNewGeogiftAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
                ActionNewGeogiftFragment dialogFragment = ActionNewGeogiftFragment.newInstance();
                dialogFragment.setPresenter(mPresenter);
                dialogFragment.show(getActivity().getFragmentManager(), ActionNewGeogiftFragment.TAG);
            }
        });

        /*mActionsListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                //** hide FAB only during the scolling (UP or DOWN)
                //** TODO : when reached the bottom hide anyway

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    onScrolledUp();
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    onScrolledDown();
                } else {
                    onScrolledUp();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });*/

        return root;
    }

    public void onScrolledDown(){
        if(!mIsFabOpen){
            mFabNewAction.startAnimation(fab_close);
            mFabNewAction.setClickable(false);
        }
    }

    public void onScrolledUp(){
        if(!mIsFabOpen){
            mFabNewAction.startAnimation(fab_open);
            mFabNewAction.setClickable(true);
        }
    }

    public void animateFAB(){
        if(mIsFabOpen){
            mFabNewAction.startAnimation(rotate_backward);

            mFabNewChatAction.startAnimation(fab_small_close);
            mFabNewGalleryAction.startAnimation(fab_small_close);
            mFabNewToDoListAction.startAnimation(fab_small_close);
            mFabNewGeogiftAction.startAnimation(fab_small_close);

            mFabNewChatAction.setClickable(false);
            mFabNewGalleryAction.setClickable(false);
            mFabNewToDoListAction.setClickable(false);
            mFabNewGeogiftAction.setClickable(false);

            mIsFabOpen = false;
            mFrameBackground.setVisibility(View.INVISIBLE);
            mFrameBackground.setClickable(false);
            mFrameBackground.setAlpha(0f);
        } else {
            mFabNewAction.startAnimation(rotate_forward);

            mFabNewChatAction.startAnimation(fab_small_open);
            mFabNewGalleryAction.startAnimation(fab_small_open);
            mFabNewToDoListAction.startAnimation(fab_small_open);
            mFabNewGeogiftAction.startAnimation(fab_small_open);

            mFabNewChatAction.setClickable(true);
            mFabNewGalleryAction.setClickable(true);
            mFabNewToDoListAction.setClickable(true);
            mFabNewGeogiftAction.setClickable(true);

            mIsFabOpen = true;
            mFrameBackground.setVisibility(View.VISIBLE);
            mFrameBackground.setClickable(true);
            mFrameBackground.setAlpha(0.5f);
        }
    }

    @Override
    public void updateActionsList(List<ActionVM> actionsVM) {
        for(ActionVM actionVM : actionsVM) {
            actionVM.setPageChanger((IPageChanger)getActivity());
            actionVM.setContext(getContext());
        }

        mActionAdapter.updateActionsList(actionsVM);
    }

    @Override
    public void registerGeofence(ArrayList<GeoItem> geoItems){
        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();

        geoFencePendingIntent = null;

        for(GeoItem geoItem : geoItems){
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(geoItem.getAddress()) //TODO change!!!
                    .setCircularRegion(Double.parseDouble(geoItem.getLat()),
                            Double.parseDouble(geoItem.getLon()),
                            GEOFENCE_RADIUS
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build());
        }

        addGeofencesOnLoad();

    }

    @Override
    public void checkGeofences() {
        //TODO if...
        if(googleApiClient!= null && googleApiClient.isConnected()){
            mPresenter.retrieveGeogift();
        }
        else {
            buildGoogleApiClient();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "createGoogleApi()");
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        googleApiClient.connect();
    }

    private GeofencingRequest getGeofencingRequest() {
        Log.d(TAG, "createGeofencingRequest");
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    public void addGeofencesOnLoad() {
        if (!googleApiClient.isConnected()) {
            Log.w(TAG, "googleApiclient not connected");
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            //logSecurityException(securityException);
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( mContext, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                mContext, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(GeoUtils.checkPermissionAccessFineLocation(mContext)) {
            mPresenter.retrieveGeogift();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "googleApi connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "googleApi connection failed");
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
    }
}