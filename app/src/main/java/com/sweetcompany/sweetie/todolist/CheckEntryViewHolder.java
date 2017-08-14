package com.sweetcompany.sweetie.todolist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.sweetcompany.sweetie.R;


/**
 * Created by lucas on 12/08/2017.
 */

public class CheckEntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    CheckBox checkBox;

    OnViewHolderClickListener mListener;

    public CheckEntryViewHolder(View itemView) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkEntry);
        checkBox.setOnClickListener(this);
    }

    void setViewHolderClickListener(OnViewHolderClickListener listener) {
        mListener = listener;
    }

    interface OnViewHolderClickListener {
        void onCheckBoxClicked(int adapterPosition, boolean isBookmarked);
    }



    @Override
    public void onClick(View v) {
        mListener.onCheckBoxClicked(getAdapterPosition(), checkBox.isChecked());
    }
    public void setText(String text) {
        checkBox.setText(text);
    }

    public void setChecked(boolean checked){
        checkBox.setChecked(checked);
    }
}
