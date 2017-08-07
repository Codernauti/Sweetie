package com.sweetcompany.sweetie.todolist;

import com.sweetcompany.sweetie.model.CheckEntryFB;

/**
 * Created by lucas on 04/08/2017.
 */

public class CheckEntryVM {
    static final boolean THE_MAIN_USER = true;
    static final boolean THE_PARTNER = false;

    private final boolean mWho;
    private final String mKey;
    private String mText;
    private final String mTime;   // Format HH:mm
    private boolean mchecked;

    public CheckEntryVM(boolean mWho, String mKey, String mText, String mTime, boolean mchecked) {
        this.mWho = mWho;
        this.mKey = mKey;
        this.mText = mText;
        this.mTime = mTime;
        this.mchecked = mchecked;
    }

    public boolean ismWho() {
        return mWho;
    }

    public String getmKey() {
        return mKey;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmTime() {
        return mTime;
    }

    public boolean isMchecked() {
        return mchecked;
    }

    public void setMchecked(boolean mchecked) {
        this.mchecked = mchecked;
    }

}
