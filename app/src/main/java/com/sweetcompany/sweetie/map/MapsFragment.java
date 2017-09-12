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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseMapController;
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

    private int currentSelectionMap = GALLERY_MAP;

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;

    private HashMap<String, Marker> galleryMarkers = new HashMap<String, Marker>();
    private HashMap<String, Marker> geogiftMarkers = new HashMap<String, Marker>();

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

        //TODO refactor
        if(currentSelectionMap == GALLERY_MAP) {
            selectGalleryMap();
        } else {
            selectGeogiftMap();
        }

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mMapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        if(currentSelectionMap == GALLERY_MAP) {
            selectGalleryMap();
        } else {
            selectGeogiftMap();
        }
    }

    private void selectGalleryMap(){
        currentSelectionMap = GALLERY_MAP;
        galleryButtonText.setTextColor(ContextCompat.getColor(mContext, R.color.rosa_sweetie));
        geogiftButtonText.setTextColor(Color.GRAY);
        galleryButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey_alpha_50));
        geogiftButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

        for (Map.Entry<String, Marker> entry : geogiftMarkers.entrySet()) {
            Marker mark = entry.getValue();
            mark.setVisible(false);
        }
        for (Map.Entry<String, Marker> entry : galleryMarkers.entrySet()) {
            Marker mark = entry.getValue();
            mark.setVisible(true);
        }
    }

    private void selectGeogiftMap(){
        currentSelectionMap = GEOGIFT_MAP;
        geogiftButtonText.setTextColor(ContextCompat.getColor(mContext, R.color.rosa_sweetie));
        galleryButtonText.setTextColor(Color.GRAY);
        geogiftButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey_alpha_50));
        galleryButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

        for (Map.Entry<String, Marker> entry : geogiftMarkers.entrySet()) {
            Marker mark = entry.getValue();
            mark.setVisible(true);
        }
        for (Map.Entry<String, Marker> entry : galleryMarkers.entrySet()) {
            Marker mark = entry.getValue();
            mark.setVisible(false);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");

        /*SupportMapFragment mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_gallery_fragment));

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

        // TODO: set smart bound

        ((DashboardActivity) getActivity()).attachMapDatabase();
    }


    // Presenter calls

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void addGallery(GalleryMapVM gallery) {
        Log.d(TAG, "addGallery()");
        if (mMap != null) {
            addGalleryMarker(gallery);
        }
    }

    private void addGalleryMarker(final GalleryMapVM gallery) {
        if( !galleryMarkers.containsKey(gallery.getKey()) ) {
            Glide.with(mContext)
                    .load(gallery.getUriCover())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(96, 96) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                        MarkerOptions galleryMarkOptions = makeGalleryMakerOptions(resource, gallery);
                        if(galleryMarkOptions != null) {
                            Marker galleryMarker = mMap.addMarker(galleryMarkOptions);
                            galleryMarker.setTitle(gallery.getTitle());
                            if (currentSelectionMap == GEOGIFT_MAP) {
                                galleryMarker.setVisible(false);
                            }
                            galleryMarkers.put(gallery.getKey(), galleryMarker);
                        }
                    }
                });
        }
    }

    private MarkerOptions makeGalleryMakerOptions(Bitmap res, GalleryMapVM gallery){
        MarkerOptions galleryMarkerOptions = null;
        if(res != null) {
            Bitmap circleBmp = UtilisGraphic.getCircleBitmap(res);
            Bitmap markerIcon = Bitmap.createScaledBitmap(circleBmp, 96, 96, false);

            LatLng latLng = new LatLng(gallery.getLat(), gallery.getLon());
            galleryMarkerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(gallery.getTitle())
                    .icon(BitmapDescriptorFactory.fromBitmap(markerIcon));
        }
        return galleryMarkerOptions;
    }

    @Override
    public void removeGallery(String galleryKey) {
        if(galleryMarkers.containsKey( galleryKey )) {
           Marker marker = galleryMarkers.get(galleryKey);
           marker.setVisible(false);
            marker.remove();
           galleryMarkers.remove(galleryKey);
        }
    }

    @Override
    public void changeGallery(GalleryMapVM gallery) {
        Log.d(TAG, "changeGallery");

        if(galleryMarkers.containsKey( gallery.getKey() )) {

            Marker marker = galleryMarkers.get( gallery.getKey() );
            marker.setVisible(false);
            marker.remove();

            galleryMarkers.remove(gallery.getKey());

            addGallery(gallery);
        }
    }


    @Override
    public void addGeogift(GeogiftMapVM geogift) {
        if(mMap != null) {

            if(!geogiftMarkers.containsKey( geogift.getKey() )) {
                MarkerOptions geogiftMarkOptions = makeGeogiftMakerOptions(geogift);

                Marker geogiftMarker = mMap.addMarker(geogiftMarkOptions);
                geogiftMarker.setTitle(geogift.getTitle());

                if (currentSelectionMap == GALLERY_MAP) {
                    geogiftMarker.setVisible(false);
                }

                geogiftMarkers.put(geogift.getKey(), geogiftMarker);
            }
        }
    }

    private MarkerOptions makeGeogiftMakerOptions(GeogiftMapVM geogift){
        LatLng latLng = new LatLng(geogift.getLat() , geogift.getLon());

        return new MarkerOptions()
                .position(latLng)
                .title(geogift.getTitle())
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("action_gift_icon",72,72)));
    }

    private Bitmap resizeBitmap(String drawableName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier(drawableName, "drawable", mContext.getPackageName()));

        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    @Override
    public void removeGeogift(String geogiftKey) {
        if(geogiftMarkers.containsKey(geogiftKey)) {
            Marker marker = geogiftMarkers.get(geogiftKey);
            marker.setVisible(false);
            marker.remove();
            geogiftMarkers.remove(geogiftKey);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallery_map_top_button: {
                selectGalleryMap();
                break;
            }
            case R.id.geogift_map_top_button: {
                selectGeogiftMap();
                break;
            }
            default:
                break;
        }
    }

}