package com.sweetcompany.sweetie.Actions;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard on 07/05/2017.
 */

class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActionViewHolder> {

    private static String TAG = "ActionsAdapter";
    private static int VIEW_HOLDER_COUNT = 0;

    private int mNumberItems;
    private List<ActionVM> mActionsList = new ArrayList<>();


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
        ActionVM actionVM = mActionsList.get(position);
        holder.title.setText(actionVM.getTitle() + " " + position);
        holder.description.setText(actionVM.getDescription());
        holder.date.setText(actionVM.getDataTime());

        // TODO switch case String only for java 7
        /*if(actionVM.getmType().equals("MSG")) {
            holder.type.setImageResource(R.drawable.mapicon_yellow);
        }
        if(actionVM.getmType().equals("PHOTO")) {
            holder.type.setImageResource(R.drawable.googleg_disabled_color_18);
        }
        if(actionVM.getmType().equals("TODO")) {
            holder.type.setImageResource(R.drawable.googleg_standard_color_18);
        }*/
    }

    @Override
    public int getItemCount() {
        return mActionsList.size();
    }

    public void updateActionsList(List<ActionVM> actionsVM) {
        mActionsList.clear();
        mActionsList.addAll(actionsVM);
        this.notifyDataSetChanged();
    }


    class ActionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, description, date;
        ImageView avatar, type;

        ActionViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            title = (TextView) itemView.findViewById(R.id.title_action_list_item);
            description = (TextView) itemView.findViewById(R.id.subtitle_action_list_item);
            date = (TextView) itemView.findViewById(R.id.date_action_list_item);
            avatar = (ImageView) itemView.findViewById(R.id.image_action_list_item);
            type = (ImageView) itemView.findViewById(R.id.type_action_list_item);
        }

        @Override
        public void onClick(View v) {
            // TODO test method
            int position = this.getAdapterPosition();
            mActionsList.get(position).showAction();
        }
    }


}
