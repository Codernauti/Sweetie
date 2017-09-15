package com.sweetcompany.sweetie.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.UtilisGraphic;

import java.util.HashMap;
import java.util.Map;

public class MapsFragment extends Fragment implements View.OnClickListener,
                                                      MapContract.View,
                                                      OnMapReadyCallback/*,
                                                      GoogleApiClient.ConnectionCallbacks,
                                                      GoogleApiClient.OnConnectionFailedListener*/{

    private static final String TAG = "MapFragment";

    private static final int GALLERY_MAP = 0;
    private static final int GEOGIFT_MAP = 1;

    private View rootView;
    private LinearLayout galleryButton;
    private LinearLayout geogiftButton;
    private TextView galleryButtonText;
    private TextView geogiftButtonText;

    private int mCurrentSelectionMap = GALLERY_MAP;

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;

    private HashMap<String, MarkerOptions> mGalleryMarkersOptions = new HashMap<>();
    private HashMap<String, MarkerOptions> mGeogiftMarkersOptions = new HashMap<>();

    private MapContract.Presenter mPresenter;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "inflate rootView");
        rootView = inflater.inflate(R.layout.maps_fragment, container, false);

        galleryButton = (LinearLayout) rootView.findViewById(R.id.gallery_map_top_button);
        galleryButton.setClickable(true);
        galleryButton.setOnClickListener(this);

        geogiftButton = (LinearLayout) rootView.findViewById(R.id.geogift_map_top_button);
        geogiftButton.setClickable(true);
        geogiftButton.setOnClickListener(this);

        galleryButtonText = (TextView) rootView.findViewById(R.id.gallery_map_top_text);
        geogiftButtonText = (TextView) rootView.findViewById(R.id.geogift_map_top_text);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mMapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        ((DashboardActivity) getActivity()).attachMapDatabase();
    }

    private void updateMarkers() {
        if (mMap != null) {
            Log.d(TAG, "update markers");
            mMap.clear();

            if (mCurrentSelectionMap == GALLERY_MAP) {

                galleryButtonText.setTextColor(ContextCompat.getColor(mContext, R.color.rosa_sweetie));
                geogiftButtonText.setTextColor(Color.GRAY);

                galleryButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey_alpha_50));
                geogiftButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

                for (Map.Entry<String, MarkerOptions> entry : mGalleryMarkersOptions.entrySet()) {
                    if (entry.getValue().getPosition() != null) {
                        MarkerOptions markerOptions = entry.getValue();
                        mMap.addMarker(markerOptions);
                    }
                }
            } else if (mCurrentSelectionMap == GEOGIFT_MAP) {
                geogiftButtonText.setTextColor(ContextCompat.getColor(mContext, R.color.rosa_sweetie));
                galleryButtonText.setTextColor(Color.GRAY);

                geogiftButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey_alpha_50));
                galleryButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

                for (Map.Entry<String, MarkerOptions> entry : mGeogiftMarkersOptions.entrySet()) {
                    MarkerOptions markerOptions = entry.getValue();
                    mMap.addMarker(markerOptions);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

        mMap.clear();
        mGalleryMarkersOptions.clear();
        mGeogiftMarkersOptions.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");

        /*SupportMapFragment mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment));

        if(mapFragment != null) {
            FragmentManager fM = getFragmentManager();
            fM.beginTransaction().remove(mapFragment).commit();
        }*/
    }


    // gms MapFragment callback

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(TAG, "onMapReady()");

        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);

        updateMarkers();

        // TODO: set smart bound on Map

        /*((DashboardActivity) getActivity()).attachMapDatabase();*/ }


    // Presenter calls

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void addGallery(GalleryMapVM gallery) {
        Log.d(TAG, "addGallery()");

        if (mMap != null && !mGalleryMarkersOptions.containsKey(gallery.getKey())) {
            buildGalleryMarker(gallery);
        }
    }

    private void buildGalleryMarker(final GalleryMapVM gallery) {

        if (gallery.getUriCover() != null &&
                gallery.getLat() != null && gallery.getLon() != null) {

            initGalleryMarkerComplete(gallery);

        } else {
            MarkerOptions markerOptions = initGalleryMarkerWithDefaultIcon(gallery);

            /*if (gallery.getLat() != null && gallery.getLon() != null) {
                markerOptions = initGalleryMarkerWithDefaultIcon(gallery);
            } else { // mozzo e senza gambe
                markerOptions = initGalleryMarkerEmpty(gallery);
            }*/

            mGalleryMarkersOptions.put(gallery.getKey(), markerOptions);
            updateMarkers();
        }
    }

    private void initGalleryMarkerComplete(final GalleryMapVM gallery) {
        Glide.with(mContext)
                .load(gallery.getUriCover())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(96, 96) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                        Bitmap circleBmp = UtilisGraphic.getCircleBitmap(resource);
                        Bitmap markerIcon = Bitmap.createScaledBitmap(circleBmp, 96, 96, false);

                        MarkerOptions markerOptions = new MarkerOptions()
                                .title(gallery.getTitle())
                                .position(new LatLng(gallery.getLat(), gallery.getLon()))
                                .icon(BitmapDescriptorFactory.fromBitmap(markerIcon));

                        mGalleryMarkersOptions.put(gallery.getKey(), markerOptions);
                        updateMarkers();
                    }
                });
    }

    private MarkerOptions initGalleryMarkerWithDefaultIcon(GalleryMapVM gallery) {
        return new MarkerOptions().title(gallery.getTitle())
                .position(new LatLng(gallery.getLat(), gallery.getLon()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
    }


    @Override
    public void removeGallery(String galleryKey) {
        if(mGalleryMarkersOptions.containsKey( galleryKey )) {
            mGalleryMarkersOptions.remove(galleryKey);
            updateMarkers();
        }
    }

    @Override
    public void changeGallery(GalleryMapVM gallery) {
        Log.d(TAG, "changeGallery");

        if (mGalleryMarkersOptions.containsKey( gallery.getKey() )) {
            Log.d(TAG, "remove, gallery already in");
            mGalleryMarkersOptions.remove(gallery.getKey());
        }

        buildGalleryMarker(gallery);
    }


    @Override
    public void addGeogift(GeogiftMapVM geogift) {
        Log.d(TAG, "addGeogift");
        if(mMap != null && !mGeogiftMarkersOptions.containsKey(geogift.getKey())) {
            mGeogiftMarkersOptions.put(geogift.getKey(), makeGeogiftMakerOptions(geogift));
            updateMarkers();
        }
    }

    private MarkerOptions makeGeogiftMakerOptions(GeogiftMapVM geogift){
        LatLng latLng = new LatLng(geogift.getLat() , geogift.getLon());

        return new MarkerOptions()
                .position(latLng)
                .title(geogift.getTitle())
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap()));
    }

    private Bitmap resizeBitmap() {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.action_gift_icon);
        return Bitmap.createScaledBitmap(imageBitmap, 72, 72, false);
    }

    @Override
    public void removeGeogift(String geogiftKey) {
        if(mGeogiftMarkersOptions.containsKey(geogiftKey)) {
            mGeogiftMarkersOptions.remove(geogiftKey);
            updateMarkers();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallery_map_top_button: {
                mCurrentSelectionMap = GALLERY_MAP;
                updateMarkers();
                break;
            }
            case R.id.geogift_map_top_button: {
                mCurrentSelectionMap = GEOGIFT_MAP;
                updateMarkers();
                break;
            }
            default:
                break;
        }
    }

}