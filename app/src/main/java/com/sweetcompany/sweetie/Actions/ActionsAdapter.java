package com.sweetcompany.sweetie.Actions;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;

import java.util.List;

/**
 * Created by Eduard on 07/05/2017.
 */

class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActionViewHolder> {

    private static String TAG = "ActionsAdapter";
    private static int VIEW_HOLDER_COUNT = 0;

    private int mNumberItems;
    private List<ActionObj> mActionsList;

    ActionsAdapter(List<ActionObj> actions) {
        mActionsList = actions;
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
        ActionObj actionObj = mActionsList.get(position);
        holder.title.setText(actionObj.getTitle() + " " + position);
        holder.description.setText(actionObj.getDescription());
        holder.date.setText(actionObj.getDataTime());

        // TODO switch case String only for java 7
        if(actionObj.getmType().equals("MSG")) {
            holder.type.setImageResource(R.drawable.mapicon_yellow);
        }
        if(actionObj.getmType().equals("PHOTO")) {
            holder.type.setImageResource(R.drawable.googleg_disabled_color_18);
        }
        if(actionObj.getmType().equals("TODO")) {
            holder.type.setImageResource(R.drawable.googleg_standard_color_18);
        }
    }

    @Override
    public int getItemCount() {
        return mActionsList.size();
    }


    class ActionViewHolder extends RecyclerView.ViewHolder{
        TextView title, description, date;
        ImageView avatar, type;

        ActionViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_action_list_item);
            description = (TextView) itemView.findViewById(R.id.subtitle_action_list_item);
            date = (TextView) itemView.findViewById(R.id.date_action_list_item);
            avatar = (ImageView) itemView.findViewById(R.id.image_action_list_item);
            type = (ImageView) itemView.findViewById(R.id.type_action_list_item);
        }

    }


}
