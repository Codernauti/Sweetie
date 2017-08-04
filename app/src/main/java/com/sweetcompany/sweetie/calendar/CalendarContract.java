package com.sweetcompany.sweetie.calendar;

import com.sweetcompany.sweetie.model.ActionDiaryFB;

import java.util.List;
import java.util.Map;

/**
 * Created by Eduard on 01-Aug-17.
 */

public interface CalendarContract {

    interface View {
        void setPresenter(Presenter presenter);

        void setMonthActionsDiary(Map<String, Map<String, ActionDiaryFB>> monthActionDiary);
    }

    interface Presenter {
        void downloadActionsDiary(String day);
        void downloadActionsDiaryForMonth(String yearAndMonth);
    }

}
