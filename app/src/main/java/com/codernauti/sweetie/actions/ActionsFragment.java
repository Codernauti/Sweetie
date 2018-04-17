package com.codernauti.sweetie.actions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Toast;

import com.codernauti.sweetie.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.List;

public class ActionsFragment extends Fragment implements ActionsContract.View, ActionsAdapter.ActionsAdapterListener {

    private static final String TAG = "ActionsFragment";

    private static final String TEMP_ACTION_UID_KEY = "tempActionUid";

    // dirty bit, we need this because the opening of ImageCrop destroy the fragment
    // and when it is recreate the onActivityResult() is called before setPresenter()
    private String mTempActionUidSelected;
    private Uri mTempActionImageUri;

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

    private ActionsContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionAdapter = new ActionsAdapter(this);
        mActionAdapter.setListener(this);
        Context context = getContext();

        //set animations
        fab_small_open = AnimationUtils.loadAnimation(context, R.anim.fab_actions_open);
        fab_small_close = AnimationUtils.loadAnimation(context, R.anim.fab_actions_close);
        fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(context, R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(context, R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(context ,R.anim.rotate_backward);

        if (savedInstanceState != null) {
            mTempActionUidSelected = savedInstanceState.getString(TEMP_ACTION_UID_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

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

    @Override
    public void setPresenter(ActionsContract.Presenter presenter) {
        Log.d(TAG, "setPresenter()");
        mPresenter = presenter;

        // code smell here, but i don't find any smart solution
        if (mTempActionUidSelected != null && !mTempActionUidSelected.isEmpty()
                && mTempActionImageUri != null) {
            // do scheduled works
            mPresenter.uploadActionImage(mTempActionUidSelected, mTempActionImageUri);
            // consume the temps variables
            mTempActionImageUri = null;
            mTempActionUidSelected = null;
        }
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
        Log.d(TAG, "onActionsListChanged");
        for(ActionVM actionVM : actionsVM) {
            // actionVM.setPageChanger((IPageChanger)getActivity());
            actionVM.setContext(getContext());
        }

        mActionAdapter.updateActionsList(actionsVM);
    }

    /*// Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case 202: //REQ_PERMISSION
            {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    Log.w(TAG, "ACCESS_FINE_LOCATION permissions granted");
                } else {
                    // Permission denied
                    Log.w(TAG, "ACCESS_FINE_LOCATION permissions denied");
                }
                break;
            }
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();

                mTempActionImageUri = resultUri;
                Toast.makeText(getContext(), "Upload image...", Toast.LENGTH_SHORT).show();

                // TODO: give more feedback to user
                /*mCoupleImage.setImageDrawable(null);
                showUploadProgress()*/;
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, error.toString());
            }
        }
    }

    // Adapter callbacks

    @Override
    public void onViewHolderLongClicked(final ActionVM action) {
        mTempActionUidSelected = action.getKey();

        ActionMenuDialogFragment dialog = ActionMenuDialogFragment.newInstance(action.getKey(),
                action.getChildType(), action.getChildUid());
        dialog.setPresenter(mPresenter);
        dialog.show(getActivity().getFragmentManager(), ActionNewChatFragment.TAG);

        // this fragment will go destroy if user select "change image"
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TEMP_ACTION_UID_KEY, mTempActionUidSelected);
    }
}