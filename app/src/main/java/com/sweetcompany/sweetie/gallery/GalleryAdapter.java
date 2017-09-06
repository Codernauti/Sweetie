package com.sweetcompany.sweetie.gallery;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 16/05/2017.
 */

class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder>
        implements MediaViewHolder.OnViewHolderClickListener{

    private static final String TAG = "GalleryAdapter";

    private List<MediaVM> mMediasList = new ArrayList<>();

    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();
    private boolean mMultipleSelectionEnable = false;

    private GalleryAdapterListener mListener;

    interface GalleryAdapterListener {
        void onPhotoClicked(int position, List<MediaVM> mediasVM);
        void onPhotoLongClicked();
        void onPhotoSelectionFinished();
    }

    /**
     * Call when create GalleryAdapter
     * @param listener
     */
    void setGalleryAdapterListener(GalleryAdapterListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
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
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        MediaVM mediaVM = mMediasList.get(position);
        mediaVM.configViewHolder(holder);

        // if viewHolder is into selectedItems change UI
        holder.setViewHolderSelected(mSelectedItems.get(position, false));

        Log.d(TAG, "onBindViewHolder(): " + mediaVM.getKey());
    }

    @Override
    public int getItemCount() {
        return mMediasList.size();
    }


    void addMedia(MediaVM media) {
        //is already in VM exchange VM
        if(searchIndexMediaOf(media) != -1)
        {
            removeMedia(media);
        }
        //TODO not clear
        mMediasList.add(0, media); //add in head
        notifyItemInserted(0);
    }

    void removeMedia(MediaVM mediaVM) {
        int indexOldMedia = searchIndexMediaOf(mediaVM);
        if (indexOldMedia != -1) {
            mMediasList.remove(indexOldMedia);
            notifyItemRemoved(indexOldMedia);
        }
    }

    void changeMedia(MediaVM mediaVM) {
        int indexOldMedia = searchIndexMediaOf(mediaVM);
        if (indexOldMedia != -1) {
            mMediasList.set(indexOldMedia, mediaVM);
            notifyItemChanged(indexOldMedia);
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

    void updatePercentUpload(String mediaUid, int perc){
        for (int indexMediaOf = 0; indexMediaOf < mMediasList.size(); indexMediaOf++) {

            String mediaKey = mMediasList.get(indexMediaOf).getKey();

            if (mediaKey.equals(mediaUid)) {
                mMediasList.get(indexMediaOf).setPercent(perc);
                notifyItemChanged(indexMediaOf);
                return;
            }
        }
    }

    // selecting of items

    void clearSelections() {
        mMultipleSelectionEnable = false;
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    List<MediaVM> getSelectedItems() {
        List<Integer> indexes = new ArrayList<>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); i++) {
            indexes.add(mSelectedItems.keyAt(i));
        }

        List<MediaVM> mediasSelected = new ArrayList<>();
        for (Integer index : indexes) {
            mediasSelected.add(mMediasList.get(index));
        }
        return mediasSelected;
    }


    // ViewHolder callback

    @Override
    public void onPhotoClicked(int adapterPosition) {
        if (mMultipleSelectionEnable) {
            toggleSelection(adapterPosition);
        } else {
            mListener.onPhotoClicked(adapterPosition, mMediasList);
        }

        if (mMultipleSelectionEnable && mSelectedItems.size() <= 0) {
            mListener.onPhotoSelectionFinished();
        }
    }

    private void toggleSelection(int pos) {
        if (mSelectedItems.get(pos, false)) {
            mSelectedItems.delete(pos);
        } else {
            mSelectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    @Override
    public void onPhotoLongClicked(int adapterPosition) {
        // tell to fragment to enable ActionMode
        mListener.onPhotoLongClicked();
        mMultipleSelectionEnable = true;

        // first element selected
        toggleSelection(adapterPosition);
    }


}
