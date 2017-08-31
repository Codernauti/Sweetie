package com.sweetcompany.sweetie.todolist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;


/**
 * Created by lucas on 12/08/2017.
 */

public class CheckEntryViewHolder extends RecyclerView.ViewHolder implements View.OnFocusChangeListener, View.OnClickListener{
    CheckBox mCheckBox;
    EditText mEditText;
    ImageButton mDeleteButton;
    OnViewHolderClickListener mListener;

    public CheckEntryViewHolder(View itemView) {
        super(itemView);
        mCheckBox = (CheckBox) itemView.findViewById(R.id.todolist_checkEntry);
        mEditText = (EditText) itemView.findViewById(R.id.todolist_editText);
        mDeleteButton = (ImageButton) itemView.findViewById(R.id.todolist_imageButton);

        mCheckBox.setOnClickListener(this);
        mEditText.setOnFocusChangeListener(this);
        mDeleteButton.setOnClickListener(this);
    }

    void setViewHolderClickListener(OnViewHolderClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.todolist_editText:
                if(!hasFocus) {
                    if(getAdapterPosition() != -1) {
                        mListener.onCheckEntryUnfocus(getAdapterPosition(), mEditText.getText().toString());
                    }
                    mDeleteButton.setVisibility(View.INVISIBLE);
                } else {
                    mDeleteButton.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    interface OnViewHolderClickListener {
        void onCheckBoxClicked(int adapterPosition, boolean isChecked);
        void onCheckEntryUnfocus(int adapterPosition,String text);
        void onCheckEntryRemove(int adapterPosition);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.todolist_checkEntry:
                mListener.onCheckBoxClicked(getAdapterPosition(), mCheckBox.isChecked());
                break;
            case R.id.todolist_imageButton:
                mListener.onCheckEntryRemove(getAdapterPosition());
        }

    }
    public void setText(String text) {
        mEditText.setText(text);
    }

    public void setChecked(boolean checked){
        mCheckBox.setChecked(checked);
    }

    public void setFocus(){
        mEditText.requestFocus();
    }

}
