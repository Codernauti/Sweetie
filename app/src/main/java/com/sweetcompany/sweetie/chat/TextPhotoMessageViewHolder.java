package com.sweetcompany.sweetie.chat;

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
    private ImageView thumbnail;
    private ProgressBar pbar;

    TextPhotoMessageViewHolder(View itemView, boolean isMainUser) {
        super(itemView);

        mPercentUploading = (TextView) itemView.findViewById(R.id.progress_percent);

        thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        thumbnail.setOnClickListener(this);

        pbar = (ProgressBar) itemView.findViewById(R.id.progressBarUpload);

        if (isMainUser) {
            mTextMessage = (EmojiconTextView) itemView.findViewById(R.id.chat_item_text_view);
            mTextTime = (TextView) itemView.findViewById(R.id.chat_item_time_text_view);
            mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_item_bookmark_button);
        }
        else {  // THE_PARTNER
            mTextMessage = (EmojiconTextView) itemView.findViewById(R.id.chat_partner_item_text_view);
            mTextTime = (TextView) itemView.findViewById(R.id.chat_partner_item_time_text_view);
            mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_partner_item_bookmark_button);
        }

        mBookmarkButton.setOnClickListener(this);
    }

    public void setText(String text) { mTextMessage.setText(text);}

    public void setTextTime(String time) {
        mTextTime.setText(time);
    }

    public void setBookmark(boolean isBookmarked) {
        mBookmarkButton.setSelected(isBookmarked);
    }

    @Override
    public void onClick(View v) {
        boolean wasBookmarked = mBookmarkButton.isSelected();
        mBookmarkButton.setSelected(!wasBookmarked);
        mListener.onBookmarkClicked(getAdapterPosition(), !wasBookmarked);
    }

    public void setPercentUploading(int progress){
        mPercentUploading.setText(progress + "%");
        if (progress >= 100){
            mPercentUploading.setVisibility(View.GONE);
            pbar.setVisibility(View.GONE);
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
                    .into(thumbnail);
        }
    }

    /*@Override
    public void onClick(View v) {
        mListener.onPhotoClicked(getAdapterPosition());
        mListener.onPhotoLongClicked(getAdapterPosition());
    }*/
}