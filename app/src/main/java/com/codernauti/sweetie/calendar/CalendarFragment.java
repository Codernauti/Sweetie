package com.codernauti.sweetie.calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.codernauti.sweetie.R;
import com.codernauti.sweetie.chatDiary.ChatDiaryActivity;
import com.codernauti.sweetie.model.ActionDiaryFB;

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
            if (mMonthActionsDiary != null) {
                return mMonthActionsDiary.containsKey(dayStr);
            } else {
                return false;
            }
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(4, ContextCompat.getColor(getContext(), R.color.rosa_sweetie)));

            /*Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.action_chat_icon_36x36);
            icon.setBounds(25, 55, 50, 80);
            ImageSpan span = new ImageSpan(icon, ImageSpan.ALIGN_BASELINE);*/

            //view.addSpan(span);

            //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_chat_icon_36x36));
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
        mCalendar.setPagingEnabled(false);

        mDayActionsDiaryList = (ListView) root.findViewById(R.id.calendar_day_actions_diary_list);

        mAdapter = new ActionsDiaryAdapter(getContext(), R.layout.action_list_item);
        mDayActionsDiaryList.setAdapter(mAdapter);

        mDayActionsDiaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                ActionDiaryVM actionDiary = mAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), ChatDiaryActivity.class);
                intent.putExtra(ChatDiaryActivity.ACTION_DIARY_DATE, actionDiary.getDate());
                intent.putExtra(ChatDiaryActivity.ACTION_DIARY_UID, actionDiary.getKey());
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            initializeActualMonth();
        }

        mCalendar.setOnMonthChangedListener(this);
        mCalendar.setOnDateChangedListener(this);
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        mCalendar.setOnMonthChangedListener(null);
        mCalendar.setOnDateChangedListener(null);

        mCalendar.clearSelection();
        mAdapter.clear();

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
    }

    // call after setPresenter()
    @Override
    public void initializeActualMonth() {
        // we need to manage two situation

        // first opening app
        // mPresenter is instantiated before fragment is attached
        // download currentMonth

        // after other activities opened
        // fragment is attached before mPresenter is instantiated
        // download mCalendarSelectedMonth

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
            List<ActionDiaryFB> actionsDiary = new ArrayList<>();

            // set ActionDiary uid and extract into an ArrayList
            for (Map.Entry<String, ActionDiaryFB> actionDiaryEntry : mMonthActionsDiary.get(day).entrySet()) {
                String actionDiaryUid = actionDiaryEntry.getKey();
                ActionDiaryFB actionDiary = actionDiaryEntry.getValue();

                actionDiary.setKey(actionDiaryUid);
                actionsDiary.add(actionDiary);
            }

            if (!actionsDiary.isEmpty()) {
                mAdapter.addAll(actionsDiary);
                Log.d(TAG, "update ListView ActionsDiary");
            }
        }
    }
}