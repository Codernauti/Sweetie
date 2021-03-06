package com.codernauti.sweetie.gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.codernauti.sweetie.R;
import com.codernauti.sweetie.utils.DataMaker;


public class SlideshowDialogFragment extends DialogFragment {

    static final String TAG = SlideshowDialogFragment.class.getSimpleName();

    private static final String IMAGES_KEY = "images";
    private static final String POSITION_KEY = "position";

    private ArrayList<PhotoVM> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };

    static SlideshowDialogFragment newInstance(List<MediaVM> medias, int position) {
        SlideshowDialogFragment f = new SlideshowDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(IMAGES_KEY, (Serializable) medias);
        bundle.putInt(POSITION_KEY, position);

        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.slideshow_viewpager);
        lblCount = (TextView) v.findViewById(R.id.slideshow_counter);
        lblTitle = (TextView) v.findViewById(R.id.slideshow_title);
        lblDate = (TextView) v.findViewById(R.id.slideshow_date);

        images = (ArrayList<PhotoVM>) getArguments().getSerializable(IMAGES_KEY);
        selectedPosition = getArguments().getInt("position");

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

        setCurrentItem(selectedPosition);
        viewPager.addOnPageChangeListener(mPageChangeListener);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());

        PhotoVM image = images.get(position);
        lblTitle.setText(image.getDescription());
        lblDate.setText(DataMaker.get_dd_MM_yy_Local(image.getTime()));
    }


    //	adapter
    private class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            PhotoVM image = images.get(position);

            Glide.with(getActivity())
                    .load(image.getUriStorage())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
