package com.sweetcompany.sweetie.Registration;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class RegisterPagerAdapter extends FragmentPagerAdapter {

    final static int STEP_ONE = 0;
    final static int STEP_TWO = 1;
    final static int STEP_THREE = 2;

    private final static int NUM_TAB = 3;


    Context context;
    RegistrationContract.Presenter mPresenter;

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
            case STEP_THREE:
                return new StepThree();
            default:

        }

        return null;
    }

    @Override
    public int getCount() {
        return NUM_TAB;
    }


}
