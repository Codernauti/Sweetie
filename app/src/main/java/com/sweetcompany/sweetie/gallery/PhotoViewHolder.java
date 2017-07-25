package com.sweetcompany.sweetie.gallery;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    public void onClick(View v) {
        mListener.onBookmarkClicked(getAdapterPosition());
    }
}