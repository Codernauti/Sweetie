package com.sweetcompany.sweetie.Registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.R;


public class RegisterActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    ViewPager mViewPager;
    RegisterPagerAdapter mAdapter;
    Context mContext;

    @Override
    //TODO usare setcustom animation e gestirseli da sè senza viewpager
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        mContext = getApplicationContext();
        mAdapter = new RegisterPagerAdapter(getSupportFragmentManager(), mContext);

        mViewPager = (ViewPager) findViewById(R.id.register_pager);
        mViewPager.setAdapter(mAdapter);
        //disable swiping // TODO checcazzè? esiste altro modo per bloccarlo?
        mViewPager.beginFakeDrag();
        mViewPager.setOffscreenPageLimit(0);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case RegisterPagerAdapter.STEP_ONE:
                mViewPager.setCurrentItem(RegisterPagerAdapter.STEP_ONE);
                break;
            case RegisterPagerAdapter.STEP_TWO:
                mViewPager.setCurrentItem(RegisterPagerAdapter.STEP_TWO);
                break;
            case RegisterPagerAdapter.STEP_THREE:
                mViewPager.setCurrentItem(RegisterPagerAdapter.STEP_THREE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void registrationCompleted(){
        startActivity(new Intent(this, DashboardActivity.class));

    }

}