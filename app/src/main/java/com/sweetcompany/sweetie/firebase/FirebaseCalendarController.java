package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.ActionDiaryFB;

import java.util.Map;

/**
 * Created by Eduard on 01-Aug-17.
 */

public class FirebaseCalendarController {

    private final DatabaseReference mCoupleCalendar;
    private DatabaseReference mLastMonthReference;
    private ValueEventListener mDayListener;

    private CalendarControllerListener mListener;


    public interface CalendarControllerListener {
        void onMonthActionsDiaryDownloaded(Map<String, Map<String, ActionDiaryFB>> monthActionsDiary);
    }


    public FirebaseCalendarController(String coupleUid) {
        mCoupleCalendar = FirebaseDatabase.getInstance()
                .getReference(Constraints.CALENDAR + "/" + coupleUid);
    }


    public void addListener(CalendarControllerListener listener) {
        mListener = listener;
    }

    public void attachNewMonthListener(String yearMonth) {
        // Only one MonthListener can be set
        this.detachListener();
        if (mDayListener == null) {
            mDayListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<Map<String, Map<String, ActionDiaryFB>>> typeIndicator =
                            new GenericTypeIndicator<Map<String, Map<String, ActionDiaryFB>>>() {};
                    Map<String, Map<String, ActionDiaryFB>> monthActionsDiary = dataSnapshot.getValue(typeIndicator);

                    if (mListener != null) {
                        mListener.onMonthActionsDiaryDownloaded(monthActionsDiary);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            };
            mLastMonthReference = mCoupleCalendar.child(yearMonth);
            mLastMonthReference.addValueEventListener(mDayListener);
            Log.d("CalendarController", "attachListener");
        }
    }

    public void detachListener() {
        if (mDayListener != null) {
            mLastMonthReference.removeEventListener(mDayListener);
            Log.d("CalendarController", "detachListener");
        }
        mDayListener = null;
    }
}
