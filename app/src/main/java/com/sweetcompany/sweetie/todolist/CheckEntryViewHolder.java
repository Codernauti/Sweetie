package com.sweetcompany.sweetie.todolist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

public class CheckEntryViewHolder extends ToDoListViewHolder implements View.OnFocusChangeListener, View.OnClickListener{
    CheckBox mCheckBox;
    EditText mEditText;
    ImageButton mDeleteButton;
    OnCheckEntryViewHolderClickListener mListener;
    String oldText;

    public CheckEntryViewHolder(View itemView) {
        super(itemView);
        mCheckBox = (CheckBox) itemView.findViewById(R.id.todolist_checkEntry);
        mEditText = (EditText) itemView.findViewById(R.id.todolist_editText);
        mDeleteButton = (ImageButton) itemView.findViewById(R.id.todolist_imageButton);

        mCheckBox.setOnClickListener(this);
        mEditText.setOnFocusChangeListener(this);
        mDeleteButton.setOnClickListener(this);
    }

    void setCheckEntryViewHolderClickListener(OnCheckEntryViewHolderClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.todolist_editText:
                if(!hasFocus) {
                    String text = mEditText.getText().toString().trim();
                    if(getAdapterPosition() != -1 && !oldText.equals(text) && text.length() != 0) {
                        mListener.onCheckEntryUnfocused(getAdapterPosition(), text);
                    }
                    mEditText.setText(text);
                    mDeleteButton.setVisibility(View.INVISIBLE);
                } else {
                    oldText = mEditText.getText().toString();
                    mDeleteButton.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    interface OnCheckEntryViewHolderClickListener {
        void onCheckBoxClicked(int adapterPosition, boolean isChecked);
        void onCheckEntryUnfocused(int adapterPosition,String text);
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
        InputMethodManager inputMethodManager =(InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
