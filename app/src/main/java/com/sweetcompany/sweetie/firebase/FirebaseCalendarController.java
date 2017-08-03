package com.sweetcompany.sweetie.firebase;

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
    private ValueEventListener mDayListener;

    private CalendarControllerListener mListener;


    public interface CalendarControllerListener {
        void onMonthActionsDiaryDownloaded(Map<String, Map<String, ActionDiaryFB>> monthActionsDiary);
        void onActionDiaryAdded(ActionDiaryFB actionDiary);
    }


    public FirebaseCalendarController(String coupleUid) {
        mCoupleCalendar = FirebaseDatabase.getInstance()
                .getReference(Constraints.CALENDAR + "/" + coupleUid);
    }


    public void addListener(CalendarControllerListener listener) {
        mListener = listener;
    }

    public void attachMonthListener(String yearMonth) {
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

            mCoupleCalendar.child(yearMonth).addValueEventListener(mDayListener);
        }

    }

    public void detachListener() {
        if (mDayListener != null) {
            mCoupleCalendar.removeEventListener(mDayListener);
        }
        mDayListener = null;
    }
}
