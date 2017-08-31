package com.sweetcompany.sweetie.gallery;

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

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.DataMaker;
import com.sweetcompany.sweetie.utils.Utility;


public class SlideshowDialogFragment extends DialogFragment {

    static final String TAG = SlideshowDialogFragment.class.getSimpleName();

    private static final String IMAGES_KEY = "images";
    private static final String POSITION_KEY = "position";

    private ArrayList<PhotoVM> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;

    private ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
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
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

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
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            PhotoVM image = images.get(position);

            String uriToLoad;
            // is image uploaded by me?
            //verify if is in Local memory and has valid path
            if(image.isTheMainUser()) {
                if(Utility.isImageAvaibleInLocal(image.getUriLocal())) uriToLoad = image.getUriLocal();
                else uriToLoad = image.getUriStorage();
            }
            else // uploaded by partner, take uri storage
            {
                uriToLoad = image.getUriStorage();
            }

            Glide.with(getActivity())
                    .load(uriToLoad)
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
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
