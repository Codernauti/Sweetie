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
    private final String key;
    private final boolean who;

    private String text;
    private final String time;   // Format HH:mm
    private boolean checked;

    public CheckEntryVM(boolean who, String key, String text, String time, boolean checked) {
        this.who = who;
        this.key = key;
        this.text = text;
        this.time = time;
        this.checked = checked;
    }


    public boolean isWho() {
        return who;
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    int getIdView() {
        return R.layout.todolist_item;
    }

    void configViewHolder(CheckEntryViewHolder viewHolder) {
        viewHolder.setText(text);
        viewHolder.setChecked(checked);
    }
}
