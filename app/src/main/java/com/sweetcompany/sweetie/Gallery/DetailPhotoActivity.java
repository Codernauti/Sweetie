package com.sweetcompany.sweetie.Gallery;

/**
 * Created by Federico on 22/05/2017.
 */

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sweetcompany.sweetie.R;


public class DetailPhotoActivity extends AppCompatActivity {

    public static final String EXTRA_DETAIL_PHOTO = "DetailPhotoActivity.DETAIL_PHOTO";

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_detail_layout);

        mImageView = (ImageView) findViewById(R.id.image);
        Photo singlePhoto = getIntent().getParcelableExtra(EXTRA_DETAIL_PHOTO);

        Glide.with(this)
                .load(singlePhoto.getUrl())
                .asBitmap()
                .error(R.drawable.action_photo_icon)
                .crossFade()
                .listener(new RequestListener<String, Bitmap>() {

                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        onPalette(Palette.from(resource).generate());
                        mImageView.setImageBitmap(resource);

                        return false;
                    }

                    public void onPalette(Palette palette) {
                        if (null != palette) {
                            ViewGroup parent = (ViewGroup) mImageView.getParent().getParent();
                            parent.setBackgroundColor(Color.BLACK);
                        }
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);

    }

}
