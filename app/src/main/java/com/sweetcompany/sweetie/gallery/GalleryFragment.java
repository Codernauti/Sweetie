package com.sweetcompany.sweetie.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.esafirm.imagepicker.features.ImagePicker;
import com.sweetcompany.sweetie.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.esafirm.imagepicker.model.Image;
import com.sweetcompany.sweetie.utils.DataMaker;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment implements GalleryContract.View, View.OnClickListener,
        GalleryAdapter.GalleryAdapterListener, ActionMode.Callback {

    private static final String TAG = "GalleryFragment";

    private static final int RC_CODE_PICKER = 2000;
    private static final int RC_CAMERA = 3000;

    private ArrayList<Image> imagesPicked = new ArrayList<>();

    private Toolbar mToolBar;
    private RecyclerView mGalleryListView;
    private FloatingActionButton mFabAddPhoto;

    private ActionMode mActionMode;

    private GalleryAdapter mGalleryAdapter;
    private GalleryContract.Presenter mPresenter;

    private String mGalleryUid;

    public static GalleryFragment newInstance(Bundle bundle) {
        GalleryFragment newGalleryFragment = new GalleryFragment();
        newGalleryFragment.setArguments(bundle);

        return newGalleryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mGalleryAdapter = new GalleryAdapter();
        mGalleryAdapter.setGalleryAdapterListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.gallery_fragment, container, false);

        String titleGallery = getArguments().getString(GalleryActivity.GALLERY_TITLE);
        mGalleryUid = getArguments().getString(GalleryActivity.GALLERY_DATABASE_KEY);

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

        mFabAddPhoto.setOnClickListener(this);

        return root;
    }

    @Override
    public void setPresenter(GalleryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateGalleryInfo(GalleryVM gallery) {
        mToolBar.setTitle(gallery.getTitle());
    }

    @Override
    public void addMedia(MediaVM mediaVM) {
        mGalleryAdapter.addMedia(mediaVM);
        mGalleryListView.scrollToPosition(0);
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
    @Deprecated
    public void updatePercentUpload(String mediaUid, int perc){
        mGalleryAdapter.updatePercentUpload(mediaUid, perc);
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (requestCode == RC_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            imagesPicked = (ArrayList<Image>) ImagePicker.getImages(data);
            sendImages();
        }
    }

    private void sendImages() {
        if (imagesPicked == null) return;

        for (int i = 0, l = imagesPicked.size(); i < l; i++) {
            Uri file = Uri.fromFile(new File(imagesPicked.get(i).getPath()));

            MediaVM newMedia = new PhotoVM(MediaVM.THE_MAIN_USER , DataMaker.get_UTC_DateTime(), "", file.toString(), null, true);
            mPresenter.sendMedia(newMedia);
        }
        imagesPicked.clear();
    }

    // Menu management

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_info_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_info: {
                Intent intent = GalleryInfoActivity.getStartActivityIntent(getContext(), mGalleryUid);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_photo: {
                takePictures();
                break;
            }
            default:
                break;
        }
    }

    private void takePictures() {
        ImagePicker.create(this)
                .theme(R.style.ImagePickerTheme)
                .returnAfterFirst(false) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle(String.valueOf(R.string.image_picker_select)) // image selection title
                .limit(10)
                .multi()
                .imageDirectory("Camera")
                .origin(imagesPicked)   // original selected images, used in multi mode
                .start(RC_CODE_PICKER);
    }


    // adapter callbacks

    @Override
    public void onPhotoClicked(int position, List<MediaVM> mediasVM) {
        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance(new ArrayList<>(mediasVM), position);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        newFragment.show(ft, SlideshowDialogFragment.TAG);
    }

    @Override
    public void onPhotoLongClicked() {
        mActionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(this);
    }

    @Override
    public void onPhotoSelectionFinished() {
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

    // ActionMode callbacks

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.gallery_cab_menu, menu);
        mFabAddPhoto.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gallery_menu_remove: {
                List<MediaVM> selectedItems = mGalleryAdapter.getSelectedItems();
                for (int i = selectedItems.size() - 1; i >= 0; i--) {
                    MediaVM media = selectedItems.get(i);
                    Log.d(TAG, "gallery item position: " + media.getKey());
                    mPresenter.removeMedia(media);
                }
                mode.finish();
                return true;
            }
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        mGalleryAdapter.clearSelections();
        mFabAddPhoto.setVisibility(View.VISIBLE);
    }
}