package com.sweetcompany.sweetie.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ghiro on 16/05/2017.
 */

class GalleryAdapter extends RecyclerView.Adapter<MediaViewHolder>
        implements MediaViewHolder.OnViewHolderClickListener{

    private static final String TAG = "GalleryAdapter";

    public List<MediaVM> mMediasList = new ArrayList<>();

    interface GalleryAdapterListener {
        void onPhotoClicked(int position, List<MediaVM> mediasVM);
        void onPhotoLongClicked(int position, List<MediaVM> mediasVM);
    }

    private GalleryAdapterListener mListener;

    /**
     * Call when create GalleryAdapter
     * @param listener
     */
    void setGalleryAdapterListener(GalleryAdapterListener listener) {
        mListener = listener;
    }

    /**
     *  Call when destroy GalleryAdapterListener
     */
    void removeChatAdapterListener() {
        mListener = null;
    }

    @Override
    public int getItemViewType(int position) {
        //TODO: this break the recycler of the viewHolder, the RecyclerView doesn't know the type
        //return position;
        return mMediasList.get(position).getIdView();
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        View viewToInflate = inflater.inflate(viewType, parent, false);
        MediaViewHolder viewHolder;

        viewHolder = new PhotoViewHolder(viewToInflate);

        viewHolder.setViewHolderClickListener(this);

        Log.d(TAG, "onCreateViewHolder(): " + viewHolder.toString());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {
        MediaVM mediaVM = mMediasList.get(position);
        mediaVM.configViewHolder(holder);

        Log.d(TAG, "onBindViewHolder(): " + mediaVM.getKey());
    }

    @Override
    public int getItemCount() {
        return mMediasList.size();
    }


    void addMedia(MediaVM media) {
        mMediasList.add(media);
        notifyItemInserted(mMediasList.size() - 1);
    }

    void removeMedia(MediaVM mediaVM) {
        int indexOldMedia = searchIndexMediaOf(mediaVM);
        if (indexOldMedia != -1) {
            mMediasList.remove(indexOldMedia);
            notifyItemRemoved(indexOldMedia);
        }
    }

    void changeMedia(MediaVM mediaVM) {
        int indexOldMessage = searchIndexMediaOf(mediaVM);
        if (indexOldMessage != -1) {
            mMediasList.set(indexOldMessage, mediaVM);
            notifyItemChanged(indexOldMessage);
        }
    }

    private int searchIndexMediaOf(MediaVM media) {
        String modifyMediaKey = media.getKey();
        for (int i = 0; i < mMediasList.size(); i++) {
            String mediaKey = mMediasList.get(i).getKey();
            if (mediaKey.equals(modifyMediaKey)) {
                return i;
            }
        }
        return -1;
    }

    void updateMediaList(List<MediaVM> mediasVM) {
        mMediasList.clear();
        mMediasList.addAll(mediasVM);
        Collections.reverse(mMediasList);
        this.notifyDataSetChanged();
    }


    /* Listener from ViewHolder */
    @Override
    public void onPhotoClicked(int adapterPosition) {
        mListener.onPhotoClicked(adapterPosition, mMediasList);
    }

    @Override
    public void onPhotoLongClicked(int adapterPosition) {

    }


}
