package com.sweetcompany.sweetie.calendar;

import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebaseCalendarController;
import com.sweetcompany.sweetie.model.ActionDiaryFB;

import java.util.Map;

/**
 * Created by Eduard on 01-Aug-17.
 */

public class CalendarPresenter implements CalendarContract.Presenter,
        FirebaseCalendarController.CalendarControllerListener {

    private static final String TAG = "CalendarPresenter";

    private final CalendarContract.View mView;
    private final FirebaseCalendarController mController;

    public CalendarPresenter(CalendarContract.View view, FirebaseCalendarController controller) {
        mController = controller;
        mController.addListener(this);

        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void downloadActionsDiaryForMonth(String yearAndMonth) {
        mController.attachNewMonthListener(yearAndMonth);
    }

    // Callback from the Controller

    @Override
    public void onMonthActionsDiaryDownloaded(Map<String, Map<String, ActionDiaryFB>> monthActionsDiary) {
        mView.setMonthActionsDiary(monthActionsDiary);
    }

    @Override
    public void onActionDiaryAdded(ActionDiaryFB actionDiary) {
        Log.d(TAG, "ActionDiary arrived! " + actionDiary.getKey() + " - size: " + actionDiary.getMessages().size());
        //mView.addActionDiary(actionDiary);
    }
}
