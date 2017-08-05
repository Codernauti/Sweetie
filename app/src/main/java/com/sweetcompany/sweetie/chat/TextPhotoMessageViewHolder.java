package com.sweetcompany.sweetie.chat;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

public class TextPhotoMessageViewHolder extends MessageViewHolder implements View.OnClickListener {
    private EmojiconTextView mTextMessage;
    private TextView mTextTime;
    private ImageButton mBookmarkButton;
    private TextView mPercentUploading;
    private ImageView mThumbnail;
    private ProgressBar mPbar;

    TextPhotoMessageViewHolder(View itemView, boolean isMainUser) {
        super(itemView);


        if (isMainUser) {
            //mTextMessage = (EmojiconTextView) itemView.findViewById(R.id.chat_item_text_view);
            mTextTime = (TextView) itemView.findViewById(R.id.chat_partner_item_photo_time_view);
            mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_item_photo_bookmark_button);
            mThumbnail = (ImageView) itemView.findViewById(R.id.chat_thumbnail_main);
            mPbar = (ProgressBar) itemView.findViewById(R.id.chat_progressBarUpload_main);
            mPercentUploading = (TextView) itemView.findViewById(R.id.chat_progress_percent_main);
        }
        else {  // THE_PARTNER
            //mTextMessage = (EmojiconTextView) itemView.findViewById(R.id.chat_partner_item_text_view);
            mTextTime = (TextView) itemView.findViewById(R.id.chat_partner_item_photo_time_view);
            mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_partner_item_photo_bookmark_button);
            mThumbnail = (ImageView) itemView.findViewById(R.id.chat_thumbnail_partner);
            mPbar = (ProgressBar) itemView.findViewById(R.id.chat_progressBarUpload_partner);
            mPercentUploading = (TextView) itemView.findViewById(R.id.chat_progress_percent_partner);
        }

        mBookmarkButton.setOnClickListener(this);
        mThumbnail.setOnClickListener(this);
    }

    public void setText(String text) {
        mTextMessage.setText(text);
    }

    public void setTextTime(String time) {
        mTextTime.setText(time);
    }

    public void setBookmark(boolean isBookmarked) {
        mBookmarkButton.setSelected(isBookmarked);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.chat_thumbnail_main:
            case R.id.chat_thumbnail_partner:
                mListener.onPhotoClicked(getAdapterPosition());
                break;

            case R.id.chat_item_photo_bookmark_button:
            case R.id.chat_partner_item_photo_bookmark_button:
                boolean wasBookmarked = mBookmarkButton.isSelected();
                mBookmarkButton.setSelected(!wasBookmarked);
                mListener.onBookmarkClicked(getAdapterPosition(), !wasBookmarked, MessageVM.TEXT_PHOTO_MSG);
                break;

            default:
                Log.d("TextPhotoMessageVH", "onClick() lost");
                break;
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