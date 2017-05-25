package com.sweetcompany.sweetie.Gallery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sweetcompany.sweetie.R;

/**
 * Created by ghiro on 25/05/2017.
 */

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

    @Override
    public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View photoView = inflater.inflate(R.layout.gallery_item_view, parent, false);

        ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

        Photo singlePhoto = mPhotos[position];
        ImageView imageView = holder.mPhotoImageView;

        Glide.with(mContext)
                .load(singlePhoto.getUrl())
                .placeholder(R.drawable.plus_icon)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return (mPhotos.length);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mPhotoImageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Photo singlePhoto = mPhotos[position];

                Intent intent = new Intent(mContext, DetailPhotoActivity.class);
                intent.putExtra(DetailPhotoActivity.EXTRA_DETAIL_PHOTO, singlePhoto);
                mContext.startActivity(intent);
            }
        }
    }

    private Photo[] mPhotos;
    private Context mContext;

    public ImageGalleryAdapter(Context context, Photo[] galleryPhotos) {
        mContext = context;
        mPhotos = galleryPhotos;
    }
}