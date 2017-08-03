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

        Calendar calendar = Calendar.getInstance();
        String currentYearAndMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());

        mPresenter.downloadActionsDiaryForMonth(currentYearAndMonth);
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
        // TODO: for every CalendarDay show icons
        mCalendar.removeDecorators();
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
    }


    // Calendar callbacks

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        Log.d(TAG, "Month changed! Attach a new listener");
        // TODO: download also 6 past month
        String yearAndMonth = new SimpleDateFormat("yyyy-MM").format(date.getDate());
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