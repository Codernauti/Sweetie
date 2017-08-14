package com.sweetcompany.sweetie.geogift;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.GeoUtils;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ghiro on 07/08/2017.
 */

public class GeogiftFragment extends Fragment implements GeogiftContract.View,
                                                         View.OnClickListener,
                                                         AdapterView.OnItemSelectedListener
                                                         {

    private static final String TAG = "GeogiftFragment";

    public static final int REQ_PERMISSION_UPDATE = 4001;
    private static final int PLACE_PICKER_REQUEST = 4002;
    private static final int RC_CODE_PICKER = 2001;

    private static final int MESSAGE_SELECTION = 0;
    private static final int PHOTO_SELECTION = 1;
    private static final int HEART_SELECTION = 2;

    private ArrayList<Image> imagesPicked = new ArrayList<>();
    private boolean isImageTaken = false;

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
   //spinner
    private Spinner timeExpirationSpinner;
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

        timeExpirationSpinner = (Spinner) root.findViewById(R.id.expiration_geogift_spinner);
        ArrayAdapter<CharSequence> adapterExpiration = ArrayAdapter.createFromResource(getContext(),
                R.array.geogift_expiration_time, android.R.layout.simple_spinner_item);
        adapterExpiration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeExpirationSpinner.setAdapter(adapterExpiration);


        /*mFabAddGeogift = (FloatingActionButton) root.findViewById(R.id.fab_new_geogift);
        mFabAddGeogift.setClickable(false);*/

        /*// Add listener
        mFabAddGeogift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        //TODO
        switchContainerGift(PHOTO_SELECTION);

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
            case R.id.image_thumb_geogift:
                if (isImageTaken){
                    // TODO
                    //showPicture();
                }
                else {
                    takePicture();
                }
                break;
            case R.id.clear_image_geogift_button:
                clearImage();
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
                if(isImageTaken) clearImageButton.setVisibility(View.VISIBLE);
                else clearImageButton.setVisibility(View.GONE);
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
            //latLngBounds = new LatLngBounds(new LatLng(44.882494, 11.601847), new LatLng(44.909004, 11.613520));
            try {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                //builder.setLatLngBounds(latLngBounds);
                Intent i = builder.build(getActivity());
                startActivityForResult(i, PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                //TODO adjust catch
                Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
                Toast toast = new Toast(mContext);
                toast.setText("GooglePlayServices Not Available");
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            } catch (Exception e) {
                Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
                Toast toast = new Toast(mContext);
                toast.setText("Error");
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            askPermissionAccessFineLocation();
        }
    }

    public void showPicture(){

    }

     public void takePicture() {
         ImagePicker imagePicker = ImagePicker.create(this)
                 .theme(R.style.ImagePickerTheme)
                 .returnAfterFirst(false) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                 .folderMode(true) // set folder mode (false by default)
                 .folderTitle("Folder") // folder selection title
                 .imageTitle(String.valueOf(R.string.image_picker_select)); // image selection title

         //imagePicker.multi();
         imagePicker.single();

         imagePicker
                   .showCamera(true) // show camera or not (true by default)
                   .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                   .origin(imagesPicked) // original selected images, used in multi mode
                   .start(RC_CODE_PICKER); // start image picker activity with request code
     }

     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
             Place place = PlacePicker.getPlace(getContext(), data);
             String address;
             LatLng latLng;
             String name;

             if (place == null) {
                 Log.i(TAG, "No place selected");
                 return;
             }else
             {
                 name = place.getName().toString();
                 address = place.getAddress().toString();
                 latLng = place.getLatLng();
             }
             locationPickerText.setText(address);
         }
         if (requestCode == RC_CODE_PICKER && resultCode == RESULT_OK && data != null) {
             imagesPicked = (ArrayList<Image>) ImagePicker.getImages(data);
             drawImage();
             return;
         }
     }

     public void drawImage(){
         if(imagesPicked.size()>0) {
             //TODO upload on cloud every time ?
             Glide.with(this).load("http://www.gstatic.com/webp/gallery/1.jpg")
                     .thumbnail(0.5f)
                     .crossFade()
                     .placeholder(R.drawable.image_placeholder)
                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                     .into(imageThumb);
             isImageTaken = true;
             clearImageButton.setVisibility(View.VISIBLE);
         }
     }

     public void clearImage(){
         if(isImageTaken){
             isImageTaken = false;
             clearImageButton.setVisibility(View.GONE);
             Glide.with(this).load(R.drawable.image_geogift_placeholder)
                     .thumbnail(0.5f)
                     .crossFade()
                     .placeholder(R.drawable.image_placeholder)
                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                     .into(imageThumb);
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

     @Override
     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

     }

     @Override
     public void onNothingSelected(AdapterView<?> parent) {

     }
 }
