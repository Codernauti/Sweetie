package com.codernauti.sweetie.geogift;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codernauti.sweetie.R;

import java.util.Random;

public class GeogiftDoneFragment extends Fragment implements  GeogiftDoneContract.View{

    private static final String TAG = "GeogiftDoneFragment";

    private static final int MESSAGE_SELECTION = 0;
    private static final int PHOTO_SELECTION = 1;
    private static final int HEART_SELECTION = 2;

    private Toolbar mToolBar;

    // polaroid container
    private FrameLayout mPolaroidFrame;
    private ImageView mImageThumb;
    private TextView mMessagePolaroidText;
    private ImageView mPinPolaroid;
    // postit
    private FrameLayout mPostitFrame;
    private TextView mMessagePostitText;
    private ImageView mPinPostit;
    // heart
    private ImageView mHeartPic;

    private Context mContext;
    private String titleGeogift;

    Random random;
    int degree;

    private GeogiftVM geoItem = null;

    private GeogiftDoneContract.Presenter mPresenter;

    public static GeogiftDoneFragment newInstance(Bundle bundle) {
        GeogiftDoneFragment newGeogiftDoneFragment = new GeogiftDoneFragment();
        newGeogiftDoneFragment.setArguments(bundle);

        return newGeogiftDoneFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

    }


    @Override
    public void setPresenter(GeogiftDoneContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.geogift_done_fragment, container, false);

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.geogift_done_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleGeogift);

        // polaroid
        mPolaroidFrame = (FrameLayout) root.findViewById(R.id.geogift_done_polaroid_container);
        mPolaroidFrame.setVisibility(View.GONE);
        mImageThumb = (ImageView) root.findViewById(R.id.geogift_done_image_thumb);
        mImageThumb.setVisibility(View.GONE);
        mMessagePolaroidText = (TextView) root.findViewById(R.id.geogift_done_polaroid_text);
        mMessagePolaroidText.setVisibility(View.GONE);
        mPinPolaroid = (ImageView) root.findViewById(R.id.geogift_done_pin_polaroid);
        mPinPolaroid.setVisibility(View.GONE);

        // postit
        mPostitFrame = (FrameLayout) root.findViewById(R.id.geogift_done_postit_container);
        mPostitFrame.setVisibility(View.GONE);
        mMessagePostitText = (TextView) root.findViewById(R.id.geogift_done_postit_text);
        mMessagePostitText.setVisibility(View.GONE);
        mPinPostit = (ImageView) root.findViewById(R.id.geogift_done_pin_postit);
        mPinPostit.setVisibility(View.GONE);

        // heart
        mHeartPic = (ImageView) root.findViewById(R.id.geogift_done_heart_picture);
        mHeartPic.setVisibility(View.GONE);

        return root;
    }

    public void drawGeogift(GeogiftVM geoitem, int type){
        switch ( type ){
            case MESSAGE_SELECTION:

                mPostitFrame.setVisibility(View.VISIBLE);

                random = new Random();
                degree = random.nextInt(6) -3;
                mPostitFrame.setRotation(degree);

                mMessagePostitText.setVisibility(View.VISIBLE);
                mPinPostit.setVisibility(View.VISIBLE);

                mMessagePostitText.setText(geoitem.getMessage());
                //mMessagePostitText.setText(String.valueOf(degree));

                break;
            case PHOTO_SELECTION:

                mPolaroidFrame.setVisibility(View.VISIBLE);

                random = new Random();
                degree = random.nextInt(6) -3;
                mPolaroidFrame.setRotation(degree);

                mImageThumb.setVisibility(View.VISIBLE);
                mMessagePolaroidText.setVisibility(View.VISIBLE);
                mPinPolaroid.setVisibility(View.VISIBLE);

                Glide.with(this).load(geoitem.getUriStorage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mImageThumb);

                mMessagePolaroidText.setText(geoitem.getMessage());

                break;
            case HEART_SELECTION:

                mHeartPic.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    public void updateGeogift(GeogiftVM geoitem) {
        Log.d(TAG, "updateGeogift");

        mToolBar.setTitle(geoitem.getTitle());

        drawGeogift(geoitem, geoitem.getType());

        if(geoitem.getIsTriggered()){

        }
        else{

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
