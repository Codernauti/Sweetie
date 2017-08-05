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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CalendarFragment extends Fragment implements CalendarContract.View,
    OnMonthChangedListener, OnDateSelectedListener {

    private static final String TAG = "CalendarFragment";

    private boolean mFirstInitialization = true;
    private SimpleDateFormat mDayFormat = new SimpleDateFormat("dd");

    private CalendarContract.Presenter mPresenter;

    private MaterialCalendarView mCalendar;
    private ListView mDayActionsDiaryList;

    private ActionsDiaryAdapter mAdapter;

    private Map<String, Map<String, ActionDiaryFB>> mMonthActionsDiary2;

    private final DayViewDecorator mDayDecorator = new DayViewDecorator() {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            String dayStr = mDayFormat.format(day.getDate());
            return mMonthActionsDiary2.containsKey(dayStr);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_chat_icon_36x36));
        }
    };;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.calendar_fragment, container, false);

        mCalendar = (MaterialCalendarView) root.findViewById(R.id.calendar);
        mDayActionsDiaryList = (ListView) root.findViewById(R.id.calendar_day_actions_diary_list);

        mAdapter = new ActionsDiaryAdapter(getContext(), R.layout.action_list_item);
        mDayActionsDiaryList.setAdapter(mAdapter);

        /*mCalendar.setOnMonthChangedListener(this);
        mCalendar.setOnDateChangedListener(this);*/

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mFirstInitialization) {
            Calendar calendar = Calendar.getInstance();
            String currentYearAndMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());

            mPresenter.downloadActionsDiaryForMonth(currentYearAndMonth);
            Log.d(TAG, "onViewCreated() first init");
            mFirstInitialization = false;
        }
        mCalendar.setOnMonthChangedListener(this);
        mCalendar.setOnDateChangedListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
        mCalendar.setOnMonthChangedListener(null);
        mCalendar.setOnDateChangedListener(null);
    }


    @Override
    public void setPresenter(CalendarContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setMonthActionsDiary(Map<String, Map<String, ActionDiaryFB>> monthActionDiary) {
        mMonthActionsDiary2 = monthActionDiary;
        mCalendar.removeDecorators();
        if (mMonthActionsDiary2 != null) {
            mCalendar.addDecorators(mDayDecorator);
            Log.d(TAG, "New mMonthActionsDiary comes: " + mMonthActionsDiary2.size());
        } else {
            Log.d(TAG, "Null mMonthActionsDiary comes");
        }
    }


    // Calendar callbacks

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        String yearAndMonth = new SimpleDateFormat("yyyy-MM").format(date.getDate());

        Log.d(TAG, "Month changed! download monthActionsDiary of: " + yearAndMonth);
        mPresenter.downloadActionsDiaryForMonth(yearAndMonth);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.d(TAG, "Date selected change! Update the ListView");
        mAdapter.clear();

        if (mMonthActionsDiary2 != null) {
            String day = mDayFormat.format(date.getDate());
            // TODO: slow operation
            List<ActionDiaryFB> actionsDiary = new ArrayList<>(mMonthActionsDiary2.get(day).values());

            if (!actionsDiary.isEmpty()) {
                mAdapter.addAll(actionsDiary);
            }
        }
    }
}