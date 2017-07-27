package com.sweetcompany.sweetie.gallery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sweetcompany.sweetie.R;

/**
 * Created by ghiro on 25/07/2017.
 */

class PhotoViewHolder extends MediaViewHolder implements View.OnClickListener {
    private TextView mTextMessage;
    private ImageView thumbnail;

    PhotoViewHolder(View itemView) {
        super(itemView);

        //mTextMessage = (TextView) itemView.findViewById(R.id.imageText);
        //mTextMessage.setOnClickListener(this);
        thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        thumbnail.setOnClickListener(this);
    }

    public void setText(String text) {
        //mTextMessage.setText(text);
        //mTextMessage.setText("ciaociaoooo");
    }

    public void setBitmap(Bitmap bitmap){
        thumbnail.setImageBitmap(bitmap);
    }

    public void setImage(String uri){
        Uri uriP;
        String stringUri = "";
        uriP = Uri.parse(stringUri);
        Glide.with(itemView.getContext()).load(uriP)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(thumbnail);
    }

    @Override
    public void onClick(View v) {
        mListener.onBookmarkClicked(getAdapterPosition());
    }
}