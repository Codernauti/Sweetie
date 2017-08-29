package com.sweetcompany.sweetie.actions;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.text.ParseException;
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
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.action_list_item, parent, false);
        ActionViewHolder viewHolder = new ActionViewHolder(view);

        //viewHolder.viewHolderIndex.setText("ViewHolder index: " + VIEW_HOLDER_COUNT);

        VIEW_HOLDER_COUNT++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: " + VIEW_HOLDER_COUNT);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ActionViewHolder holder, int position) {
        ActionVM actionVM = mActionsList.get(position);
        holder.title.setText(actionVM.getTitle());
        holder.description.setText(actionVM.getDescription());
        //holder.date.setText(DataMaker.get_dd_MM_Local(actionVM.getDataTime()));
        try {
            holder.date.setText(DataMaker.get_Date_4_Action(actionVM.getDataTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.type.setImageResource(actionVM.getIconId());
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
