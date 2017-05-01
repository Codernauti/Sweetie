package com.sweetcompany.sweetie.Home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.Auth;
import com.sweetcompany.sweetie.LoginActivity;
import com.sweetcompany.sweetie.R;

public class HomeActivity extends AppCompatActivity {

    ViewPager mViewPager;
    HomePagerAdapter mAdapter;
    Context mContext;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mContext = getApplicationContext();
        mAdapter = new HomePagerAdapter(getSupportFragmentManager(), mContext);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setupTopTabber();

    }

    private void setupTopTabber() {
        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //Set map tab icon
        tabLayout.getTabAt(3).setIcon(R.drawable.mapicon_white);
        // Show HomePage first
        mViewPager.setCurrentItem(HomePagerAdapter.HOME_TAB);
        //SlidingTabStrip in TabLayout
        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        //Last tab (map) in SlidingTabStrip
        View tab1 = slidingTabStrip.getChildAt(3);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab1.getLayoutParams();
        layoutParams.weight = 0.5f;
        tab1.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
