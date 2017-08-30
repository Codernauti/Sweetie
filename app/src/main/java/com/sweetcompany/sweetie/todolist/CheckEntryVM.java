package com.sweetcompany.sweetie.todolist;

import android.widget.CheckBox;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.model.CheckEntryFB;

/**
 * Created by lucas on 04/08/2017.
 */

public class CheckEntryVM {
    static final boolean THE_MAIN_USER = true;
    static final boolean THE_PARTNER = false;
    private final String mKey;
    private final boolean mWho;
    private String mText;
    private final String mTime;   // Format HH:mm
    private boolean mChecked;


    private boolean mFocus;

    public CheckEntryVM(boolean who, String key, String text, String time, boolean checked) {
        this.mWho = who;
        this.mKey = key;
        this.mText = text;
        this.mTime = time;
        this.mChecked = checked;
        this.mFocus = true;
    }


    public boolean isWho() {
        return mWho;
    }

    public String getKey() {
        return mKey;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public String getTime() {
        return mTime;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        this.mChecked = checked;
    }

    public boolean isFocus() {
        return mFocus;
    }

    public void setFocus(boolean mFocus) {
        this.mFocus = mFocus;
    }

    int getIdView() {
        return R.layout.todolist_item;
    }

    void configViewHolder(CheckEntryViewHolder viewHolder) {
        viewHolder.setText(mText);
        viewHolder.setChecked(mChecked);
        if(mFocus) {
            viewHolder.setFocus();
        } else {
            mFocus = true;
        }
    }
}
