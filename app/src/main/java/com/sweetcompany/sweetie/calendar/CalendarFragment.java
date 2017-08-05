package com.sweetcompany.sweetie.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.model.ActionDiaryFB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class CalendarFragment extends Fragment implements CalendarContract.View,
    OnMonthChangedListener, OnDateSelectedListener {

    private static final String TAG = "CalendarFragment";

    private final SimpleDateFormat mDayFormat = new SimpleDateFormat("dd");
    private final SimpleDateFormat mYearMonthFormat = new SimpleDateFormat("yyyy-MM");

    private CalendarContract.Presenter mPresenter;

    private MaterialCalendarView mCalendar;
    private ListView mDayActionsDiaryList;

    private ActionsDiaryAdapter mAdapter;

    private Map<String, Map<String, ActionDiaryFB>> mMonthActionsDiary;

    private final DayViewDecorator mDayDecorator = new DayViewDecorator() {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            String dayStr = mDayFormat.format(day.getDate());
            return mMonthActionsDiary.containsKey(dayStr);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_chat_icon_36x36));
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");
        if (mPresenter == null) {
            Log.d(TAG, "mPresenter is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.calendar_fragment, container, false);

        mCalendar = (MaterialCalendarView) root.findViewById(R.id.calendar);
        mDayActionsDiaryList = (ListView) root.findViewById(R.id.calendar_day_actions_diary_list);

        mAdapter = new ActionsDiaryAdapter(getContext(), R.layout.action_list_item);
        mDayActionsDiaryList.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mCalendar.setOnMonthChangedListener(this);
        mCalendar.setOnDateChangedListener(this);
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        mCalendar.setOnMonthChangedListener(null);
        mCalendar.setOnDateChangedListener(null);
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach()");
    }

    @Override
    public void setPresenter(CalendarContract.Presenter presenter) {
        mPresenter = presenter;
        initializeActualMonth();
    }

    private void initializeActualMonth() {
        // we need to manage two situation

        // first opening app
        // mPresenter is instantiated before fragment is attached
        // download currentMonth

        // after other activities opened
        // fragment is attached before mPresenter is instantiated
        // download mCalendarSelectedMonth

        // mPresenter cannot be null, this call come from setPresenter()

        // default value is current month
        String currentSelectedYearAndMonth = mYearMonthFormat.format(Calendar.getInstance().getTime());

        if (mCalendar != null) {
            currentSelectedYearAndMonth = mYearMonthFormat.format(mCalendar.getCurrentDate().getDate());
        }

        Log.d(TAG, "Manual month initialization");
        removeDecoratorAndDownloadActionsDiaryForMonth(currentSelectedYearAndMonth);
    }

    private void removeDecoratorAndDownloadActionsDiaryForMonth(String yearAndMonth) {
        if (mCalendar != null) {
            mCalendar.removeDecorators();
        }
        mPresenter.downloadActionsDiaryForMonth(yearAndMonth);
    }


    @Override
    public void setMonthActionsDiary(Map<String, Map<String, ActionDiaryFB>> monthActionDiary) {
        mMonthActionsDiary = monthActionDiary;
        if (mMonthActionsDiary != null) {
            mCalendar.addDecorators(mDayDecorator);
            Log.d(TAG, "New mMonthActionsDiary comes: " + mMonthActionsDiary.size());
        } else {
            Log.d(TAG, "Null mMonthActionsDiary comes");
        }
    }

    // Calendar callbacks

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        String yearAndMonth = mYearMonthFormat.format(date.getDate());

        Log.d(TAG, "Month changed! download monthActionsDiary of: " + yearAndMonth);
        removeDecoratorAndDownloadActionsDiaryForMonth(yearAndMonth);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.d(TAG, "Date selected change!");
        mAdapter.clear();

        String day = mDayFormat.format(date.getDate());
        if (mMonthActionsDiary != null && mMonthActionsDiary.get(day) != null) {
            // TODO: slow operation
            List<ActionDiaryFB> actionsDiary = new ArrayList<>(mMonthActionsDiary.get(day).values());

            if (!actionsDiary.isEmpty()) {
                mAdapter.addAll(actionsDiary);
                Log.d(TAG, "update ListView ActionsDiary");
            }
        }
    }
}