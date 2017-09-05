package com.sweetcompany.sweetie.calendar;

import android.graphics.drawable.Drawable;

import com.sweetcompany.sweetie.R;

/**
 * Created by Eduard on 02-Aug-17.
 */

public interface ActionDiaryVM {
    String getKey();
    int getType();
    String getDate();
    String getDescription();
    String getTitle();
    String getImageUri();
}
