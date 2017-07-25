package com.sweetcompany.sweetie.gallery;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.GestureDetector;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.List;
        import com.sweetcompany.sweetie.R;

/**
 * Created by Lincoln on 31/03/16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private static final String TAG = "GalleryAdapter";

    private List<MediaVM> mMediasList = new ArrayList<>();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //MediaVM mediaVM = mMediasList.get(position);
        PhotoVM photoVM = (PhotoVM) mMediasList.get(position);

        /*Glide.with(mContext).load(image.getMedium())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);*/

        holder.thumbnail.setImageBitmap(photoVM.getBitmap());
    }

    @Override
    public int getItemCount() {
        return mMediasList.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    void addMedia(MediaVM mediaVM) {
        mMediasList.add(mediaVM);
        notifyItemInserted(mMediasList.size() - 1);
    }

    void removeMedia(MediaVM mediaVM) {
        int indexOldPhoto = searchIndexMediaOf(mediaVM);
        if (indexOldPhoto != -1) {
            mMediasList.remove(indexOldPhoto);
            notifyItemRemoved(indexOldPhoto);
        }
    }

    void changeMedia(MediaVM mediaVM) {
        int indexOldPhoto = searchIndexMediaOf(mediaVM);
        if (indexOldPhoto != -1) {
            mMediasList.set(indexOldPhoto, mediaVM);
            notifyItemChanged(indexOldPhoto);
        }
    }

    private int searchIndexMediaOf(MediaVM mediaVM) {
        String modifyMsgKey = mediaVM.getKey();
        for (int i = 0; i < mMediasList.size(); i++) {
            String msgKey = mMediasList.get(i).getKey();
            if (msgKey.equals(modifyMsgKey)) {
                return i;
            }
        }
        return -1;
    }

    void updateMediaList(List<MediaVM> messagesVM) {
        mMediasList.clear();
        mMediasList.addAll(messagesVM);
        Collections.reverse(mMediasList);
        this.notifyDataSetChanged();
    }
}