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

import com.google.firebase.auth.FirebaseAuth;
import com.sweetcompany.sweetie.actions.ActionsPresenter;
import com.sweetcompany.sweetie.couple.CoupleActivity;
import com.sweetcompany.sweetie.couple.CoupleDetailsActivity;
import com.sweetcompany.sweetie.firebase.FirebaseActionsController;
import com.sweetcompany.sweetie.firebase.FirebaseCalendarController;
import com.sweetcompany.sweetie.gallery.GalleryActivity;
import com.sweetcompany.sweetie.pairing.PairingActivity;
import com.sweetcompany.sweetie.utils.Utility;

public class DashboardActivity extends AppCompatActivity implements IPageChanger {

    private ViewPager mViewPager;
    private DashboardPagerAdapter mAdapter;
    private Context mContext;
    private TabLayout tabLayout;

    private FirebaseActionsController mActionsController;
    private FirebaseCalendarController mCalendarController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        mContext = getApplicationContext();

        // init the Controllers for fragments
        String coupleUid = Utility.getStringPreference(this, Utility.COUPLE_UID);

        mActionsController = new FirebaseActionsController(coupleUid);
        mCalendarController = new FirebaseCalendarController(coupleUid);
        // mMapsController = ...

        mAdapter = new DashboardPagerAdapter(getSupportFragmentManager(), mContext,
                mActionsController, mCalendarController);

        mViewPager = (ViewPager) findViewById(R.id.dashboard_pager);
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
    protected void onResume() {
        super.onResume();
        mActionsController.attachNetworkDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: clean up adapter?
        mActionsController.detachNetworkDatabase();
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
            case R.id.menu_sign_out:
                // TODO: extract these lines of code
                Utility.saveStringPreference(this, Utility.USER_UID, "error");
                Utility.saveStringPreference(this, Utility.MAIL, "error");
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.menu_fake_gallery:
                startActivity(new Intent(this, GalleryActivity.class));
                return true;
            case R.id.menu_couple_details:
                startActivity(new Intent(this, CoupleDetailsActivity.class));
                return true;
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
