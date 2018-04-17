package com.codernauti.sweetie.todolist;

import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.codernauti.sweetie.R;



public class CheckEntryViewHolder extends ToDoListViewHolder implements View.OnFocusChangeListener, View.OnClickListener, View.OnKeyListener{
    private CheckBox mCheckBox;
    private EditText mEditText;
    private ImageButton mDeleteButton;
    private OnCheckEntryViewHolderClickListener mListener;
    private String oldText;

    public CheckEntryViewHolder(View itemView) {
        super(itemView);

        mEditText = (EditText) itemView.findViewById(R.id.todolist_editText);
        mDeleteButton = (ImageButton) itemView.findViewById(R.id.todolist_imageButton);
        mCheckBox = (CheckBox) itemView.findViewById(R.id.todolist_checkEntry);

        mEditText.setOnFocusChangeListener(this);
        mEditText.setOnKeyListener(this);
        mCheckBox.setOnClickListener(this);
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    mListener.onEnterKeyPressed();
                    return true;
            }
            return false;
        }
        return false;
    }


    interface OnCheckEntryViewHolderClickListener {
        void onCheckBoxClicked(int adapterPosition, boolean isChecked);
        void onCheckEntryUnfocused(int adapterPosition,String text);
        void onCheckEntryRemove(int adapterPosition);
        void onEnterKeyPressed();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.todolist_checkEntry:
                String attualText = mEditText.getText().toString().trim();
                if(oldText != attualText){
                    mListener.onCheckEntryUnfocused(getAdapterPosition(), attualText);
                }
                mListener.onCheckBoxClicked(getAdapterPosition(), mCheckBox.isChecked());
                break;
            case R.id.todolist_imageButton:
                mListener.onCheckEntryRemove(getAdapterPosition());
        }

    }
    public void setText(String text) {
        mEditText.setText(text);
        mEditText.setSelection(text.length());
    }

    public void setChecked(boolean checked){
        mCheckBox.setChecked(checked);
    }

    public void setFocus(){
        mEditText.requestFocus();
    }
}
