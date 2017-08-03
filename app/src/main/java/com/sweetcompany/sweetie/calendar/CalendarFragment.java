package com.sweetcompany.sweetie.calendar;

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
import com.sweetcompany.sweetie.utils.DataMaker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class CalendarFragment extends Fragment implements CalendarContract.View,
    OnMonthChangedListener, OnDateSelectedListener {

    private static final String TAG = "CalendarFragment";

    private CalendarContract.Presenter mPresenter;

    private MaterialCalendarView mCalendar;
    private ListView mDayActionsDiaryList;

    private ActionsDiaryAdapter mAdapter;
    private Map<String, List<ActionDiaryVM>> mMonthActionsDiary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.calendar_fragment, container, false);

        mCalendar = (MaterialCalendarView) root.findViewById(R.id.calendar);
        mDayActionsDiaryList = (ListView) root.findViewById(R.id.calendar_day_actions_diary_list);

        mAdapter = new ActionsDiaryAdapter(getContext(), R.layout.action_list_item);
        mDayActionsDiaryList.setAdapter(mAdapter);

        mCalendar.setOnMonthChangedListener(this);
        mCalendar.setOnDateChangedListener(this);


        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            Calendar calendar = Calendar.getInstance();
            String currentYearAndMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());

            mPresenter.downloadActionsDiaryForMonth(currentYearAndMonth);
            Log.d(TAG, "onViewCreated() first time");
        } else {
            Log.d(TAG, "onViewCreated() not first time");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPresenter(CalendarContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void addActionDiary(ActionDiaryVM actionDiary) {
        mAdapter.add(actionDiary);
    }

    @Override
    public void setMonthActionsDiary(final Map<String, List<ActionDiaryVM>> monthActionDiary) {
        mMonthActionsDiary = monthActionDiary;
        mCalendar.removeDecorators();
        // TODO: for every CalendarDay show icons
        if (mMonthActionsDiary != null) {
            mCalendar.addDecorators(new DayViewDecorator() {
                @Override
                public boolean shouldDecorate(CalendarDay day) {
                    String dayStr = new SimpleDateFormat("dd").format(day.getDate());
                    return monthActionDiary.containsKey(dayStr);
                }

                @Override
                public void decorate(DayViewFacade view) {
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_chat_icon));
                }
            });
            Log.d(TAG, "New mMonthActionsDiary comes: " + mMonthActionsDiary.size());
        } else {
            Log.d(TAG, "Null mMonthActionsDiary comes");
        }

    }


    // Calendar callbacks

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        mCalendar.removeDecorators();
        // TODO: download also 6 past month
        String yearAndMonth = new SimpleDateFormat("yyyy-MM").format(date.getDate());

        Log.d(TAG, "Month changed! download monthActionsDiary of: " + yearAndMonth);
        mPresenter.downloadActionsDiaryForMonth(yearAndMonth);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.d(TAG, "Date selected change! Update the ListView");
        mAdapter.clear();

        if (mMonthActionsDiary != null) {
            String day = new SimpleDateFormat("dd").format(date.getDate());
            List<ActionDiaryVM> actionsDiary = mMonthActionsDiary.get(day);

            if (actionsDiary != null) {
                mAdapter.addAll(actionsDiary);
            }
        }
    }
}