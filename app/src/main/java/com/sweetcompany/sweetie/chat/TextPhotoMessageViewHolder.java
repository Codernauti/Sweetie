package com.sweetcompany.sweetie.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sweetcompany.sweetie.R;

import io.github.rockerhieu.emojicon.EmojiconTextView;

/**
 * Created by ghiro on 03/08/2017.
 */

public class TextPhotoMessageViewHolder extends MessageViewHolder {
    private EmojiconTextView mTextMessage;
    private TextView mTextTime;
    /*private ImageButton mBookmarkButton;*/
    private TextView mPercentUploading;
    private ImageView mThumbnail;
    private ProgressBar mPbar;

    TextPhotoMessageViewHolder(View itemView) {
        super(itemView);

        // viewIds are the same between the user and the partner
        //mTextMessage = (EmojiconTextView) itemView.findViewById(R.id.chat_item_text_view);
        mTextTime = (TextView) itemView.findViewById(R.id.chat_item_photo_time_view);
        /*mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_item_photo_bookmark_button);*/
        mThumbnail = (ImageView) itemView.findViewById(R.id.chat_thumbnail);
        mPbar = (ProgressBar) itemView.findViewById(R.id.chat_progressBarUpload);
        mPercentUploading = (TextView) itemView.findViewById(R.id.chat_progress_percent);

        mThumbnail.setOnClickListener(this);
    }

    public void setText(String text) {
        mTextMessage.setText(text);
    }

    public void setTextTime(String time) {
        mTextTime.setText(time);
    }

    @Override
    int getType() {
        return MessageVM.TEXT_PHOTO_MSG;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.chat_thumbnail) {
            super.photoClicked();
        } else {
            super.onClick(v);
        }
    }

    public void setPercentUploading(int progress){
        mPercentUploading.setText(progress + "%");
        if (progress >= 100){
            mPercentUploading.setVisibility(View.GONE);
            mPbar.setVisibility(View.GONE);
        }
    }

    public void setImage(String uri){
        // TODO: useless checking, if uri is empty glide put a image placeholder
        if(!uri.equals("")) {
            //Uri uriP;
            //uriP = Uri.parse(uri);

            Glide.with(itemView.getContext()).load(uri)
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mThumbnail);
        }
    }
}