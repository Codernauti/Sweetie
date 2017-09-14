package com.sweetcompany.sweetie.calendar;

import com.sweetcompany.sweetie.firebase.FirebaseCalendarController;
import com.sweetcompany.sweetie.model.ActionDiaryFB;

import java.util.Map;


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
        mView.initializeActualMonth();
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

}
