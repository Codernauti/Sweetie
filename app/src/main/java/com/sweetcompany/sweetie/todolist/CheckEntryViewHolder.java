package com.sweetcompany.sweetie.todolist;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;


/**
 * Created by lucas on 12/08/2017.
 */

public class CheckEntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    CheckBox checkBox;
    TextView textView;

    OnViewHolderClickListener mListener;

    public CheckEntryViewHolder(View itemView) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkEntry);
        textView = (TextView) itemView.findViewById(R.id.todolist_textView);

        checkBox.setOnClickListener(this);
        textView.setOnLongClickListener(this);
    }

    void setViewHolderClickListener(OnViewHolderClickListener listener) {
        mListener = listener;
    }



    interface OnViewHolderClickListener {
        void onCheckBoxClicked(int adapterPosition, boolean isChecked);
        void onCheckBoxLongClicked(int adapterPosition);
    }

    @Override
    public boolean onLongClick(View v) {
        mListener.onCheckBoxLongClicked(getAdapterPosition());
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.todolist_checkBox:
                mListener.onCheckBoxClicked(getAdapterPosition(), checkBox.isChecked());
                break;
        }

    }
    public void setText(String text) {
        textView.setText(text);
    }

    public void setChecked(boolean checked){
        checkBox.setChecked(checked);
    }

}
