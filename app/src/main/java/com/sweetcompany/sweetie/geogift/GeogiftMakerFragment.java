package com.sweetcompany.sweetie.geogift;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ghiro on 07/08/2017.
 */

public class GeogiftMakerFragment extends Fragment implements
                                                         View.OnClickListener,
                                                         GeogiftMakerContract.View,
                                                         AdapterView.OnItemSelectedListener {

    private static final String TAG = "GeogiftMakerFragment";

    private static final int PLACE_PICKER_REQUEST = 4002;

    // key for Intent extras
    public static final String GEOGIFT_ADDESS_PICKED = "GeogiftAdressPicked";
    public static final String GEOGIFT_LAT_PICKED = "GeogiftLatPicked";
    public static final String GEOGIFT_LON_PICKED = "GeogiftLonPicked";
    public static final String GEOGIFT_ITEM_SELECTION = "GeogiftAdressSelection";
    public static final String GEOGIT_PHOTO_PICKED = "GeogiftPhotoPicked";

    private static final int MESSAGE_SELECTION = 0;
    private static final int PHOTO_SELECTION = 1;
    private static final int HEART_SELECTION = 2;
    private static final int MIN_MESSAGE_LENGHT = 0;
    private int currentSelection = 1;
    private boolean mIsButtonsEnable = true;

    private ArrayList<Image> imagesPicked = new ArrayList<>();
    private boolean isImageTaken = false;

    private Toolbar mToolBar;
    //location picker topbar
    private ImageView locationPickerIcon;
    private TextView mLocationPickerText;
    //item selector bar
    private View mMessageButton;
    private View mPhotoButton;
    private View mHeartButton;
    private ImageView mMessageIcon;
    private ImageView mPhotoIcon;
    private ImageView mHeartIcon;
    //image selector container
    private FrameLayout mPolaroidFrame;
    private ImageView mImageThumb;
    private ImageView mClearImageButton;
    private EditText mMessagePolaroidEditText;
    // postit
    private FrameLayout mPostitFrame;
    private EditText mMessagePostitEditText;
    // heart
    private ImageView mHeartPic;
    //spinner
    //private Spinner timeExpirationSpinner;
    //fabButton
    private ImageButton mSendGeogift;
    //uploading fragment
    private View mSendingFragment;
    private TextView uploadingPercent;

    private String messageGeogift = "";
    private LatLng positionGeogift = null;
    private String addressGeogift;
    private Double latGeogift = 0d;
    private Double lonGeogift = 0d;
    private String stringUriLocal;
    private String uriImageToLoad;
    private String uriStorage;

    private boolean isGeogiftComplete = false;

    private Context mContext;
    private GeogiftMakerContract.Presenter mPresenter;
    private String titleGeogift;

    private GeogiftVM mGeoItem;

    public static GeogiftMakerFragment newInstance(Bundle bundle) {
        GeogiftMakerFragment newGeogiftMakerFragment = new GeogiftMakerFragment();
        newGeogiftMakerFragment.setArguments(bundle);

        return newGeogiftMakerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.geogift_maker_fragment, container, false);

        // TODO: is useless to set titleGeogift, Firebase update it also if it is offline
        titleGeogift = getArguments().getString(GeogiftMakerActivity.GEOGIFT_TITLE);
        Log.d(TAG, "from Intent GEOGIFT_TITLE: " + titleGeogift);

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.geogift_maker_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleGeogift);

        // loaction picker
        locationPickerIcon = (ImageView) root.findViewById(R.id.geogift_icon_topbar);
        locationPickerIcon.setOnClickListener(this);
        mLocationPickerText = (TextView) root.findViewById(R.id.geogift_textview_topbar);
        mLocationPickerText.setOnClickListener(this);

        // selector bar
        mMessageButton = (View) root.findViewById(R.id.message_geogift_layout);
        mMessageButton.setOnClickListener(this);
        mPhotoButton = (View) root.findViewById(R.id.photo_geogift_layout);
        mPhotoButton.setOnClickListener(this);
        mHeartButton = (View) root.findViewById(R.id.heart_geogift_layout);
        mHeartButton.setOnClickListener(this);
        mMessageIcon = (ImageView) root.findViewById(R.id.geogift_message_icon);
        mPhotoIcon = (ImageView) root.findViewById(R.id.geogift_photo_icon) ;
        mHeartIcon = (ImageView) root.findViewById(R.id.geogift_heart_icon);

        // polaroid
        mPolaroidFrame = (FrameLayout) root.findViewById(R.id.geogift_polaroid_container);
        mPolaroidFrame.setVisibility(View.GONE);
        mImageThumb = (ImageView) root.findViewById(R.id.geogift_image_thumb);
        mImageThumb.setVisibility(View.GONE);
        mImageThumb.setOnClickListener(this);
        mClearImageButton = (ImageView) root.findViewById(R.id.geogift_clear_image_button);
        mClearImageButton.setVisibility(View.GONE);
        mClearImageButton.setOnClickListener(this);

        mMessagePolaroidEditText = (EditText) root.findViewById(R.id.geogift_polaroid_edit_text);
        mMessagePolaroidEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                messageGeogift = s.toString();
                checkGeogiftFields();
            }
        });
        mMessagePolaroidEditText.setVisibility(View.GONE);

        // postit
        mPostitFrame = (FrameLayout) root.findViewById(R.id.geogift_postit_container);
        mPostitFrame.setVisibility(View.GONE);

        mMessagePostitEditText = (EditText) root.findViewById(R.id.geogift_postit_edit_text);
        mMessagePostitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                messageGeogift = s.toString();
                checkGeogiftFields();
            }
        });
        mMessagePolaroidEditText.setVisibility(View.GONE);

        // heart
        mHeartPic = (ImageView) root.findViewById(R.id.geogift_heart_picture);
        mHeartPic.setVisibility(View.GONE);

        /*timeExpirationSpinner = (Spinner) root.findViewById(R.id.expiration_geogift_spinner);
        ArrayAdapter<CharSequence> adapterExpiration = ArrayAdapter.createFromResource(getContext(),
                R.array.geogift_expiration_time, android.R.layout.simple_spinner_item);
        adapterExpiration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeExpirationSpinner.setAdapter(adapterExpiration);*/

        // uploading
        mSendingFragment = (View) root.findViewById(R.id.included_uploading_geogift);
        uploadingPercent = (TextView) mSendingFragment.findViewById(R.id.uploading_percent_geogift_text);

        // fab button
        mSendGeogift = (ImageButton) root.findViewById(R.id.geogift_send_button);
        mSendGeogift.setClickable(true);

        switchContainerGift(PHOTO_SELECTION);

        mGeoItem = new GeogiftVM();

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
    public void onClick(View v) {
        if(mIsButtonsEnable == true) {
            switch (v.getId()) {
                case R.id.geogift_icon_topbar:
                case R.id.geogift_textview_topbar:
                    pickPosition();
                    break;
                case R.id.message_geogift_layout:
                    switchContainerGift(MESSAGE_SELECTION);
                    break;
                case R.id.photo_geogift_layout:
                    switchContainerGift(PHOTO_SELECTION);
                    break;
                case R.id.heart_geogift_layout:
                    switchContainerGift(HEART_SELECTION);
                    break;
                case R.id.geogift_image_thumb:
                    if (!isImageTaken) {
                        takePicture();
                    }
                    break;
                case R.id.geogift_clear_image_button:
                    clearImage();
                    break;
                case R.id.geogift_send_button:
                    if (isGeogiftComplete) {
                        prepareNewGeogift();
                        mIsButtonsEnable = false;
                        mMessagePolaroidEditText.clearFocus();
                        mMessagePostitEditText.clearFocus();
                        mMessagePolaroidEditText.setEnabled(false);
                        mMessagePostitEditText.setEnabled(false);
                    }
                default:
                    break;
            }
        }
    }

    public void switchContainerGift(int item) {
        switch ( item ){
            case MESSAGE_SELECTION:

                mMessageIcon.setAlpha(1f);
                mPhotoIcon.setAlpha(0.5f);
                mHeartIcon.setAlpha(0.5f);

                mPolaroidFrame.setVisibility(View.GONE);
                mImageThumb.setVisibility(View.GONE);
                mClearImageButton.setVisibility(View.GONE);
                mMessagePolaroidEditText.setVisibility(View.GONE);
                mPostitFrame.setVisibility(View.VISIBLE);
                mMessagePostitEditText.setVisibility(View.VISIBLE);
                mHeartPic.setVisibility(View.GONE);

                currentSelection = MESSAGE_SELECTION;
                checkGeogiftFields();
                break;
            case PHOTO_SELECTION:

                mMessageIcon.setAlpha(0.5f);
                mPhotoIcon.setAlpha(1f);
                mHeartIcon.setAlpha(0.5f);

                mPolaroidFrame.setVisibility(View.VISIBLE);
                mImageThumb.setVisibility(View.VISIBLE);
                if(isImageTaken) mClearImageButton.setVisibility(View.VISIBLE);
                else mClearImageButton.setVisibility(View.GONE);
                mMessagePolaroidEditText.setVisibility(View.VISIBLE);
                mPostitFrame.setVisibility(View.GONE);
                mMessagePostitEditText.setVisibility(View.GONE);
                mHeartPic.setVisibility(View.GONE);

                currentSelection = PHOTO_SELECTION;
                checkGeogiftFields();
                break;
            case HEART_SELECTION:

                mMessageIcon.setAlpha(0.5f);
                mPhotoIcon.setAlpha(0.5f);
                mHeartIcon.setAlpha(1f);

                mPolaroidFrame.setVisibility(View.GONE);
                mImageThumb.setVisibility(View.GONE);
                mClearImageButton.setVisibility(View.GONE);
                mMessagePolaroidEditText.setVisibility(View.GONE);
                mPostitFrame.setVisibility(View.GONE);
                mMessagePostitEditText.setVisibility(View.GONE);
                mHeartPic.setVisibility(View.VISIBLE);

                currentSelection = HEART_SELECTION;
                checkGeogiftFields();
                break;
        }
    }

    public void checkGeogiftFields(){
        switch (currentSelection){
            case MESSAGE_SELECTION:
                if(messageGeogift.length()>MIN_MESSAGE_LENGHT){
                    isGeogiftComplete = true;
                }
                else
                {
                    isGeogiftComplete = false;
                }
                break;
            case PHOTO_SELECTION:
                if (isImageTaken){
                    isGeogiftComplete = true;
                }
                else{
                    isGeogiftComplete = false;
                }
                break;
            case HEART_SELECTION:
                isGeogiftComplete = true;
                break;
        }

        if(positionGeogift == null){
            isGeogiftComplete = false;
        }

        if(isGeogiftComplete){
            mSendGeogift.setClickable(true);
            mSendGeogift.setAlpha(1.0f);
        }
        else{
            mSendGeogift.setClickable(false);
            mSendGeogift.setAlpha(0.5f);
        }
    }

    public void pickPosition(){
        //latLngBounds = new LatLngBounds(new LatLng(44.882494, 11.601847), new LatLng(44.909004, 11.613520));
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            // TODO set start bounds
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

     public void takePicture() {
         CropImage.activity()
                 .setAspectRatio(256,256)
                 .start(getContext(), this);
     }

     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
             Place place = PlacePicker.getPlace(getContext(), data);
             LatLng latLng;
             String name;

             if (place == null) {
                 Log.i(TAG, "No place selected");
                 return;
             }else
             {
                 name = place.getName().toString();
                 addressGeogift = place.getAddress().toString();
                 mLocationPickerText.setText(addressGeogift);
                 latLng = place.getLatLng();
                 positionGeogift = new LatLng(latLng.latitude, latLng.longitude);
                 checkGeogiftFields();
             }
         }
         if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

             CropImage.ActivityResult result = CropImage.getActivityResult(data);

             if (resultCode == RESULT_OK) {
                 Uri resultUri = result.getUri();

                 stringUriLocal = resultUri.toString();

                 drawImage();
             }
             else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                 Exception error = result.getError();
                 Log.d(TAG, error.toString());
             }
         }
     }

     public void drawImage() {
         uriImageToLoad = stringUriLocal;

             Glide.with(this).load(uriImageToLoad)
                     .placeholder(R.drawable.image_geogift_placeholder)
                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                     .into(mImageThumb);

             isImageTaken = true;
             mClearImageButton.setVisibility(View.VISIBLE);
             checkGeogiftFields();

     }

     public void clearImage() {
         if(isImageTaken){
             isImageTaken = false;
             mClearImageButton.setVisibility(View.GONE);
             imagesPicked.clear();
             Glide.with(this).load(R.drawable.image_geogift_placeholder)
                     .placeholder(R.drawable.image_placeholder)
                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                     .into(mImageThumb);
             checkGeogiftFields();
         }
     }

    // TODO
    //Spinner expiration time
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

     }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

     }
    @Override
    public void setPresenter(GeogiftMakerContract.Presenter presenter) {
        mPresenter = presenter;
    }
    @Override
    public void updatePercentUpload(int perc) {
        uploadingPercent.setText(perc+"%");
    }

    @Override
    public void setUriStorage(String uriS) {
        //call when image is uploaded
        uriStorage = uriS;
        if(uriStorage!=null)
        {
            mGeoItem.setUriStorage(uriStorage);
            createNewGeogift();
        }
    }

    public void prepareNewGeogift() {
        if(currentSelection == PHOTO_SELECTION){
            mPresenter.uploadMedia(uriImageToLoad);
            mSendingFragment.setVisibility(View.VISIBLE);
            mGeoItem.setType(GeogiftVM.PHOTO_GEOGIFT);
            //createNewGeogift();
        }
        else if(currentSelection == MESSAGE_SELECTION){
            mGeoItem.setType(GeogiftVM.MESSAGE_GEOGIFT);
            createNewGeogift();
        }
        else if(currentSelection == HEART_SELECTION){
            mGeoItem.setType(GeogiftVM.HEART_GEOGIFT);
            createNewGeogift();
        }
    }

    public void createNewGeogift() {

        //TODO implement GeogiftVM with costructor not only setter
       String mUserUid = Utility.getStringPreference(mContext, SharedPrefKeys.USER_UID);
       mGeoItem.setUserCreatorUID(mUserUid);
       mGeoItem.setAddress(addressGeogift);
       mGeoItem.setMessage(messageGeogift);
       mGeoItem.setBookmarked(false); //TODO
       mGeoItem.setLat(positionGeogift.latitude);
       mGeoItem.setLon(positionGeogift.longitude);


       List<String> keys = mPresenter.pushGeogiftAction(mGeoItem, titleGeogift, mUserUid);

       if (keys != null) {
           //backpress
           // TODO ?????
           getActivity().finish();
       }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(GEOGIFT_ADDESS_PICKED, addressGeogift);
        savedInstanceState.putDouble(GEOGIFT_LAT_PICKED, latGeogift);
        savedInstanceState.putDouble(GEOGIFT_LON_PICKED, lonGeogift);
        savedInstanceState.putInt(GEOGIFT_ITEM_SELECTION, currentSelection);
        savedInstanceState.putString(GEOGIT_PHOTO_PICKED, stringUriLocal);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            addressGeogift = savedInstanceState.getString(GEOGIFT_ADDESS_PICKED);
            if(addressGeogift != null) mLocationPickerText.setText(addressGeogift);

            latGeogift = savedInstanceState.getDouble(GEOGIFT_LAT_PICKED);
            lonGeogift = savedInstanceState.getDouble(GEOGIFT_LON_PICKED);
            if(latGeogift!= 0d && lonGeogift != 0d){
                positionGeogift = new LatLng(latGeogift, lonGeogift);
            }

            stringUriLocal = savedInstanceState.getString(GEOGIT_PHOTO_PICKED);
            if(stringUriLocal != null) {
                isImageTaken = true;
                drawImage();
            }

            int selectionItem = savedInstanceState.getInt(GEOGIFT_ITEM_SELECTION);
            switchContainerGift(selectionItem);
        }
    }

}
