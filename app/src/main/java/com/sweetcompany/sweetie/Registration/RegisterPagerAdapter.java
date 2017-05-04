package com.sweetcompany.sweetie.Registration;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sweetcompany.sweetie.Actions.ActionsFragment;
import com.sweetcompany.sweetie.Calendar.CalendarFragment;
import com.sweetcompany.sweetie.Folders.FoldersFragment;
import com.sweetcompany.sweetie.Map.MapFragment;
import com.sweetcompany.sweetie.R;


public class RegisterPagerAdapter extends FragmentPagerAdapter {

    final static int STEP_ONE = 0;
    final static int STEP_TWO = 1;

    private final static int NUM_TAB = 2;


    Context context;

    RegisterPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        context = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case STEP_ONE:
                return new StepOne();
            case STEP_TWO:
                return new StepTwo();
        }

        return null;
    }

    @Override
    public int getCount() {
        return NUM_TAB;
    }


}
