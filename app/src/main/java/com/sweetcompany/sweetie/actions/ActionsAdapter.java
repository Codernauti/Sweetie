package com.sweetcompany.sweetie.actions;


import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard on 07/05/2017.
 */

class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActionViewHolder> {

    private static String TAG = "ActionsAdapter";
    private static int VIEW_HOLDER_COUNT = 0;

    private List<ActionVM> mActionsList = new ArrayList<>();
    private final Fragment mFragment;

    private ActionsAdapterListener mListener;

    interface ActionsAdapterListener {
        void onViewHolderLongClicked(ActionVM action);
    }

    void setListener(ActionsAdapterListener listener) {
        mListener = listener;
    }

    ActionsAdapter(Fragment fragment) {
        mFragment = fragment;
    }

    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.action_list_item, parent, false);
        ActionViewHolder viewHolder = new ActionViewHolder(view);

        VIEW_HOLDER_COUNT++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: " + VIEW_HOLDER_COUNT);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ActionViewHolder holder, int position) {
        ActionVM actionVM = mActionsList.get(position);
        actionVM.configViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return mActionsList.size();
    }

    @Deprecated
    void updateActionsList(List<ActionVM> actionsVM) {
        mActionsList.clear();
        mActionsList.addAll(actionsVM);
        this.notifyDataSetChanged();
    }


    class ActionViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private final TextView mNoImageTextView;
        private TextView mTitleTextView,
                        mDescriptionTextView,
                        mDateTextView;
        private ImageView mAvatarImageView,
                        mTypeIcon;

        private String mTitleText;

        ActionViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.action_item_title);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.action_item_subtitle);
            mDateTextView = (TextView) itemView.findViewById(R.id.action_item_date);

            mAvatarImageView = (ImageView) itemView.findViewById(R.id.image_action_list_item);
            mNoImageTextView = (TextView) itemView.findViewById(R.id.action_no_image_text);

            mTypeIcon = (ImageView) itemView.findViewById(R.id.type_action_list_item);
        }

        void setTitle(String title) {
            mTitleText = title;
            mTitleTextView.setText(title);
        }

        void setDescription(String description) {
            mDescriptionTextView.setText(description);
        }

        void setDateTime(String dateTime) {
            mDateTextView.setText(dateTime);
        }

        void setAvatar(String uri) {
            Glide.with(mFragment)
                    .load(uri)
                    .placeholder(R.color.action_avatar_background)
                    .dontAnimate()
                    .into(mAvatarImageView);

            if (uri == null) {
                for (int index = 4; index > 0; index--) {
                    if (mTitleText.length() > index) {
                        mNoImageTextView.setText(mTitleText.substring(0, index) + ".");
                        index = 0;
                    } else if (mTitleText.length() == index) {
                        mNoImageTextView.setText(mTitleText.substring(0, index));
                        index = 0;
                    }
                }
                mNoImageTextView.setVisibility(View.VISIBLE);
            } else {
                mNoImageTextView.setVisibility(View.GONE);
            }
        }

        void setTypeIcon(int typeIcon) {
            mTypeIcon.setImageResource(typeIcon);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            mActionsList.get(position).showAction();
        }

        @Override
        public boolean onLongClick(View v) {
            if (mListener != null) {
                mListener.onViewHolderLongClicked(mActionsList.get(getAdapterPosition()));
            }
            return true;
        }
    }


}
