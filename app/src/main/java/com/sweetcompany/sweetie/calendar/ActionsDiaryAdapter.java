package com.sweetcompany.sweetie.calendar;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.sweetcompany.sweetie.model.ActionDiaryFB;

/**
 * Created by Eduard on 01-Aug-17.
 */

public class ActionsDiaryAdapter extends ArrayAdapter<ActionDiaryFB> {

    public ActionsDiaryAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
}
