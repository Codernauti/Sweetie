package com.sweetcompany.sweetie.gallery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ImagePickerActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.actions.ActionNewChatFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import com.esafirm.imagepicker.model.Image;
import com.sweetcompany.sweetie.utils.DataMaker;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ghiro on 22/07/2017.
 */

public class GalleryFragment extends Fragment implements GalleryContract.View, View.OnClickListener,
        GalleryAdapter.GalleryAdapterListener{

    private static final String TAG = "ChatFragment";
    int PICK_IMAGE_REQUEST = 111;
    private static final int RC_CODE_PICKER = 2000;
    private static final int RC_CAMERA = 3000;

    private ArrayList<Image> imagesPicked = new ArrayList<>();
    private List<MediaVM> medias = new ArrayList<>();

    private Toolbar mToolBar;
    private RecyclerView mGalleryListView;
    private LinearLayoutManager mLinearLayoutManager;

    private GalleryAdapter mGalleryAdapter;
    private GalleryContract.Presenter mPresenter;

    private FloatingActionButton mFabAddPhoto;

    public static GalleryFragment newInstance(Bundle bundle) {
        GalleryFragment newGalleryFragment = new GalleryFragment();
        newGalleryFragment.setArguments(bundle);

        return newGalleryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGalleryAdapter = new GalleryAdapter();
        mGalleryAdapter.setGalleryAdapterListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.gallery_fragment, container, false);

        String titleGallery = getArguments().getString(GalleryActivity.GALLERY_TITLE);
        Log.d(TAG, "from Intent GALLERY_TITLE: " +
                getArguments().getString(GalleryActivity.GALLERY_TITLE));
        Log.d(TAG, "from Intent GALLERY_DATABASE_KEY: " +
                getArguments().getString(GalleryActivity.GALLERY_DATABASE_KEY));

        mGalleryListView = (RecyclerView) root.findViewById(R.id.gallery_list);

        mToolBar = (Toolbar) root.findViewById(R.id.gallery_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleGallery);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 3);
        mGalleryListView.setLayoutManager(mLayoutManager);
        mGalleryListView.setItemAnimator(new DefaultItemAnimator());
        mGalleryListView.setAdapter(mGalleryAdapter);

        mFabAddPhoto = (FloatingActionButton) root.findViewById(R.id.fab_add_photo);
        mFabAddPhoto.setClickable(false);

        mFabAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictures();
            }
        });

        return root;
    }

    @Override
    public void setPresenter(GalleryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void updateMediaList(List<MediaVM> mediaVM) {
        mGalleryAdapter.updateMediaList(mediaVM);
    }

    @Override
    public void updateGalleryInfo(GalleryVM gallery) {
        mToolBar.setTitle(gallery.getTitle());
    }

    @Override
    public void updateMedia(MediaVM mediaVM) {
        mGalleryAdapter.addMedia(mediaVM);
    }

    @Override
    public void removeMedia(MediaVM mediaVM) {
        mGalleryAdapter.removeMedia(mediaVM);
    }

    @Override
    public void changeMedia(MediaVM mediaVM) {
        mGalleryAdapter.changeMedia(mediaVM);
    }

    @Override
    public void updatePercentUpload(MediaVM mediaVM, int perc){
        mGalleryAdapter.updatePercentUpload(mediaVM, perc);
    }

    public void takePictures() {

        ImagePicker imagePicker = ImagePicker.create(this)
                .theme(R.style.ImagePickerTheme)
                .returnAfterFirst(false) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle(String.valueOf(R.string.image_picker_select)); // image selection title

        //imagePicker.multi(); // multi mode (default mode)
        imagePicker.single();

        imagePicker.limit(10) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .origin(imagesPicked) // original selected images, used in multi mode
                .start(RC_CODE_PICKER); // start image picker activity with request code
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (requestCode == RC_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            imagesPicked = (ArrayList<Image>) ImagePicker.getImages(data);
            sendImages(imagesPicked);
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void sendImages(List<Image> images) {
        if (images == null) return;

        for (int i = 0, l = images.size(); i < l; i++) {
            MediaVM newMedia = null;

            Uri file = Uri.fromFile(new File(images.get(i).getPath()));
            String stringUriLocal;
            stringUriLocal = file.toString();

            newMedia = new PhotoVM(MediaVM.THE_MAIN_USER , DataMaker.get_UTC_DateTime(), "", null, stringUriLocal, "", 0);
            mPresenter.sendMedia(newMedia);
        }
    }

    //TODO implement gesture single and long click

    @Override
    public void onPhotoClicked(int position, List<MediaVM> mediasVM) {
        medias = mediasVM;
        Bundle bundle = new Bundle();
        bundle.putSerializable("images", (Serializable) medias);
        bundle.putInt("position", position);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
        newFragment.setArguments(bundle);
        newFragment.show(ft, "slideshow");
    }

    @Override
    public void onPhotoLongClicked(int position, List<MediaVM> mediasVM) {

    }
}