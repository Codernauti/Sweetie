package com.sweetcompany.sweetie.calendar;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.model.ActionFB;

/**
 * Created by Eduard on 01-Aug-17.
 */

public class ActionsDiaryAdapter extends ArrayAdapter<ActionDiaryVM> {

    public ActionsDiaryAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ActionDiaryViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // build the view of ActionDiary
            convertView = inflater.inflate(R.layout.action_list_item, parent, false);

            viewHolder = new ActionDiaryViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.action_item_title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.action_item_subtitle);
            viewHolder.type = (ImageView) convertView.findViewById(R.id.type_action_list_item);

            convertView.findViewById(R.id.action_item_date).setVisibility(View.GONE);
            convertView.findViewById(R.id.image_action_list_item).setVisibility(View.GONE);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ActionDiaryViewHolder) convertView.getTag();
        }

        ActionDiaryVM actionDiary = getItem(position);
        viewHolder.title.setText(actionDiary.getTitle());
        viewHolder.description.setText(actionDiary.getDescription());
        if (actionDiary.getType() == ActionFB.CHAT) {
            viewHolder.type.setImageResource(R.drawable.action_chat_icon);
        }

        return convertView;
    }

    static private class ActionDiaryViewHolder {
        TextView title;
        TextView description;
        TextView date;
        /*ImageView avatar;*/
        ImageView type;

        ActionDiaryViewHolder() {
            /*title = (TextView) itemView.findViewById(R.id.title_action_list_item);
            description = (TextView) itemView.findViewById(R.id.subtitle_action_list_item);
            date = (TextView) itemView.findViewById(R.id.date_action_list_item);
            avatar = (ImageView) itemView.findViewById(R.id.image_action_list_item);
            type = (ImageView) itemView.findViewById(R.id.type_action_list_item);*/
        }
    }
}
