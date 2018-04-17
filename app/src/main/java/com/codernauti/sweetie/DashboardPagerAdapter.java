package com.codernauti.sweetie;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.codernauti.sweetie.actions.ActionsContract;
import com.codernauti.sweetie.actions.ActionsPresenter;
import com.codernauti.sweetie.calendar.CalendarContract;
import com.codernauti.sweetie.calendar.CalendarFragment;
import com.codernauti.sweetie.calendar.CalendarPresenter;
import com.codernauti.sweetie.firebase.FirebaseActionsController;
import com.codernauti.sweetie.firebase.FirebaseCalendarController;
import com.codernauti.sweetie.firebase.FirebaseMapController;
import com.codernauti.sweetie.actions.ActionsFragment;
import com.codernauti.sweetie.map.MapContract;
import com.codernauti.sweetie.map.MapPresenter;
import com.codernauti.sweetie.map.MapsFragment;


public class DashboardPagerAdapter extends FragmentPagerAdapter {

    private final static int NUM_TAB = 3;

    final static int CALENDAR_TAB = 0;
    final static int HOME_TAB = 1;
    //final static int FOLDERS_TAB = 2;
    final static int MAP_TAB = 2;

    // For getString From Resource
    private final Context mContext;
    String mUserID;

    private final FirebaseActionsController mActionsController;
    private ActionsContract.Presenter mActionsPresenter;

    private final FirebaseCalendarController mCalendarController;
    private CalendarContract.Presenter mCalendarPresenter;

    private final FirebaseMapController mMapController;
    private MapContract.Presenter mMapPresenter;

    DashboardPagerAdapter(FragmentManager fm, Context context,
                          FirebaseActionsController actionsController,
                          FirebaseCalendarController calendarController,
                          FirebaseMapController mapController,
                          String userUID) {

        super(fm);
        mContext = context;

        mActionsController = actionsController;
        mCalendarController = calendarController;
        mMapController = mapController;
        mUserID = userUID;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case CALENDAR_TAB:
                return new CalendarFragment();
            case HOME_TAB:
                return new ActionsFragment();
            //case FOLDERS_TAB:
              //  return new FoldersFragment();
            case MAP_TAB:
                return new MapsFragment();
        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object createdFragment = super.instantiateItem(container, position);
        // instantiate presenters here
        switch (position) {
            case CALENDAR_TAB:
                CalendarContract.View calendarView = (CalendarContract.View) createdFragment;
                mCalendarPresenter = new CalendarPresenter(calendarView, mCalendarController);
                Log.d("CalendarFragment", "instantiate Presenter");
                break;
            case HOME_TAB:
                ActionsContract.View view = (ActionsContract.View) createdFragment;
                mActionsPresenter = new ActionsPresenter(view, mActionsController, mUserID);
                Log.d("ActionsFragment", "instantiate Presenter");
                break;
            //case FOLDERS_TAB:
              //  break;
            case MAP_TAB:
                MapContract.View mapView = (MapContract.View) createdFragment;
                mMapPresenter = new MapPresenter(mapView, mMapController);
                Log.d("MapFragment", "instantiate Presenter");
                break;
            default:
                Log.d("DashboardPagerAdapter", "No fragment position found!");
                break;
        }
        return createdFragment;
    }

    @Override
    public int getCount() {
        return NUM_TAB;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case CALENDAR_TAB:
                return mContext.getString(R.string.calendar_tab_name);
            case HOME_TAB:
                return mContext.getString(R.string.home_tab_name);
            //case FOLDERS_TAB:
              //  return mContext.getString(R.string.folders_tab_name);
            case MAP_TAB:
                return mContext.getString(R.string.map_tab_name);
        }
        return null;
    }
}
