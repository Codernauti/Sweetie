package com.sweetcompany.sweetie;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DashboardActivity extends AppCompatActivity implements IPageChanger {

    ViewPager mViewPager;
    DashboardPagerAdapter mAdapter;
    Context mContext;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        mContext = getApplicationContext();
        mAdapter = new DashboardPagerAdapter(getSupportFragmentManager(), mContext);

        mViewPager = (ViewPager) findViewById(R.id.dashboard_pager);
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
        mViewPager.setCurrentItem(DashboardPagerAdapter.HOME_TAB);
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
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                finish();
                return true;
            case R.id.fake_update_menu:
                mAdapter.mPresenter.updateActionsList();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void changePageTo(int page) {
        mViewPager.setCurrentItem(page);
    }

    // esco dall'app
    //TODO guardare se è il metodo migliore per uscire dall'app
    public void onBackPressed() {
        //  super.onBackPressed();
        moveTaskToBack(true);
    }
}
