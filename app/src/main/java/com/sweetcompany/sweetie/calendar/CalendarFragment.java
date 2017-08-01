package com.sweetcompany.sweetie.calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.sweetcompany.sweetie.R;

import java.text.SimpleDateFormat;


public class CalendarFragment extends Fragment implements CalendarContract.View,
    OnMonthChangedListener, OnDateSelectedListener {

    private static final String TAG = "CalendarFragment";

    private CalendarContract.Presenter mPresenter;

    private MaterialCalendarView mCalendar;
    private ListView mDayActionsDiaryList;

    private ActionsDiaryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.calendar_fragment, container, false);

        mCalendar = (MaterialCalendarView) root.findViewById(R.id.calendar);
        mDayActionsDiaryList = (ListView) root.findViewById(R.id.calendar_day_actions_diary_list);

        mAdapter = new ActionsDiaryAdapter(getContext(), R.layout.action_list_item);

        mCalendar.setOnMonthChangedListener(this);
        mCalendar.setOnDateChangedListener(this);


        return root;
    }


    @Override
    public void setPresenter(CalendarContract.Presenter presenter) {
        mPresenter = presenter;
    }


    // Calendar callbacks

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        Log.d(TAG, "Month changed! Attach a new listener");
        // TODO: download only 6 past month
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.d(TAG, "Date selected change! Update the ListView");
        // TODO: set a listener for download that DiaryAction
        String plainDate = new SimpleDateFormat("yyyy-MM-dd").format(date.getDate());
        mPresenter.downloadActionsDiary(plainDate);
    }
}