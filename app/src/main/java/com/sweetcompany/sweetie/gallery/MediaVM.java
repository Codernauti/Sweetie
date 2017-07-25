package com.sweetcompany.sweetie.gallery;

import android.view.View;

/**
 * Created by ghiro on 25/07/2017.
 */

abstract class MediaVM {
    static final boolean THE_MAIN_USER = true;
    static final boolean THE_PARTNER = false;

    private final String mKey;

    private final boolean mWho;
    private final String mTime;
    private String description;// Format HH:mm
    private boolean mBookMarked;

    MediaVM(boolean who, String date, String desc, boolean bookMarked, String key) {
        mWho = who;
        mTime = date;
        description = desc;
        mBookMarked = bookMarked;
        mKey = key;
    }

    boolean isTheMainUser() {
        return mWho;
    }

    String getTime() {
        return mTime;
    }

    String getDescription(){ return description;}
    void setDescription(String desc){ description = desc;}

    boolean isBookmarked() {return mBookMarked; }
    void setBookmarked(boolean bookmarked) {mBookMarked = bookmarked; }

    String getKey() { return mKey; }

    abstract void configViewHolder(MediaViewHolder viewHolder);

    abstract int getIdView();

    @Deprecated
    abstract MediaViewHolder newViewHolder(View inflatedView);

}
