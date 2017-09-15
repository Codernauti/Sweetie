package com.sweetcompany.sweetie;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.sweetcompany.sweetie.couple.CoupleDetailsActivity;
import com.sweetcompany.sweetie.firebase.FirebaseActionsController;
import com.sweetcompany.sweetie.firebase.FirebaseCalendarController;
import com.sweetcompany.sweetie.firebase.FirebaseMapController;
import com.sweetcompany.sweetie.geogift.GeogiftTestActivity;
import com.sweetcompany.sweetie.map.MapsFragment;
import com.sweetcompany.sweetie.pairing.PairingActivity;
import com.sweetcompany.sweetie.settings.SettingsActivity;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

public class DashboardActivity extends BaseActivity implements IPageChanger {

    private static final String TAG = "DashboardActivity";

    private final int NUM_TAB = 3;

    private ViewPager mViewPager;
    private DashboardPagerAdapter mAdapter;
    private Context mContext;
    private TabLayout tabLayout;

    private FirebaseActionsController mActionsController;
    private FirebaseCalendarController mCalendarController;
    private FirebaseMapController mMapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        mContext = getApplicationContext();

        // init the Controllers for fragments
        mActionsController = new FirebaseActionsController(super.mCoupleUid, super.mUserUid);
        mCalendarController = new FirebaseCalendarController(super.mCoupleUid);
        mMapController = new FirebaseMapController(super.mCoupleUid, super.mUserUid);

        mAdapter = new DashboardPagerAdapter(getSupportFragmentManager(), mContext,
                mActionsController, mCalendarController, mMapController, super.mUserUid);

        mViewPager = (ViewPager) findViewById(R.id.dashboard_pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(myToolbar);

        setupTopTabber();
    }

    private void setupTopTabber() {
        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //Set map tab icon
        //tabLayout.getTabAt(DashboardPagerAdapter.MAP_TAB).setIcon(R.drawable.mapicon_white);
        // Show HomePage first
        mViewPager.setCurrentItem(DashboardPagerAdapter.HOME_TAB);
        //SlidingTabStrip in TabLayout
        /*ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        //Last tab (map) in SlidingTabStrip
        View tab1 = slidingTabStrip.getChildAt(DashboardPagerAdapter.MAP_TAB);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab1.getLayoutParams();
        layoutParams.weight = 0.5f;
        tab1.setLayoutParams(layoutParams);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onResume");
        mActionsController.attachNetworkDatabase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onPause");

        mActionsController.detachNetworkDatabase();
        mCalendarController.detachListener();
        mMapController.detachNetworkDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        if (super.mCoupleUid.equals(SharedPrefKeys.DEFAULT_VALUE)) {
            menu.findItem(R.id.menu_couple_details).setVisible(false);
        } else {
            menu.findItem(R.id.menu_search_partner).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                FirebaseAuth.getInstance().signOut();   // trigger mAuthListener into BaseActivity
                return true;

            case R.id.menu_search_partner:
                startActivity(new Intent(this, PairingActivity.class));
                return true;
            case R.id.menu_couple_details:
                startActivity(new Intent(this, CoupleDetailsActivity.class));
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            /*case R.id.menu_fake_geogift:
                startActivity(new Intent(this, GeogiftTestActivity.class));
                return true;*/

            /*case R.id.menu_print_sp:
                Utility.printLogAllSharedPreferences(this);
                return true;*/

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

    // GoogleApi

    public void attachMapDatabase() {
        Log.d(TAG, "MapFragment request attach");
        mMapController.attachNetworkDatabase();
    }
}
