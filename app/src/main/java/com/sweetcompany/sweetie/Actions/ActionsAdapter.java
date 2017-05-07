package com.sweetcompany.sweetie.Actions;

import android.content.Context;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;

/**
 * Created by Eduard on 07/05/2017.
 */

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActionViewHolder> {

    private static String TAG = "ActionsAdapter";
    private static int VIEW_HOLDER_COUNT = 0;

    private int mNumberItems;

    public ActionsAdapter(int numberOfItems) {
        mNumberItems = numberOfItems;
        //mOnClickListener = listener;
    }

    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.action_list_item, viewGroup, false);
        ActionViewHolder viewHolder = new ActionViewHolder(view);

        //viewHolder.viewHolderIndex.setText("ViewHolder index: " + VIEW_HOLDER_COUNT);

        VIEW_HOLDER_COUNT++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: " + VIEW_HOLDER_COUNT);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ActionViewHolder holder, int position) {
        Log.d(TAG, ":onBindViewHolder() #" + position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    public class ActionViewHolder extends RecyclerView.ViewHolder {
        public ActionViewHolder(View itemView) {
            super(itemView);
        }
    }
}
