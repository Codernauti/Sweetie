package com.sweetcompany.sweetie.Gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.sweetcompany.sweetie.Gallery.ImageGalleryAdapter;
import com.sweetcompany.sweetie.R;

/**
 * Created by Federico on 22/05/2017.
 */

public class GalleryActivity extends AppCompatActivity {

    private ImageGalleryAdapter mGalleryAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_grid_view);

        mGalleryAdapter = new ImageGalleryAdapter(this, Photo.getPhotos());
        mContext = getApplicationContext();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mGalleryAdapter);

    }

}

