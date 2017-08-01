package com.sweetcompany.sweetie.calendar;

import android.util.Log;

import com.sweetcompany.sweetie.firebase.FirebaseCalendarController;
import com.sweetcompany.sweetie.model.ActionDiaryFB;

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
    public void downloadActionsDiary(String day) {
        mController.detachListener();
        mController.attachListener(day);
    }

    @Override
    public void onActionDiaryAdded(ActionDiaryFB actionDiary) {
        Log.d(TAG, "ActionDiary arrived! " + actionDiary.getKey() + " - size: " + actionDiary.getMessages().size());
    }
}
