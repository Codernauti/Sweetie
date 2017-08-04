package com.sweetcompany.sweetie.calendar;

import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebaseCalendarController;
import com.sweetcompany.sweetie.model.ActionDiaryFB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        mView = view;
        mView.setPresenter(this);

        mController = controller;
        mController.addListener(this);
    }

    @Override
    public void downloadActionsDiary(String yearMonth) {
        /*mController.detachListener();
        mController.attachMonthListener(yearMonth);*/
    }

    @Override
    public void downloadActionsDiaryForMonth(String yearAndMonth) {
        mController.detachListener();
        mController.attachMonthListener(yearAndMonth);
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
