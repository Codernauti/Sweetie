package com.sweetcompany.sweetie.calendar;

import java.util.List;
import java.util.Map;

/**
 * Created by Eduard on 01-Aug-17.
 */

public interface CalendarContract {

    interface View {
        void setPresenter(Presenter presenter);

        void addActionDiary(ActionDiaryVM actionDiary);
        void setMonthActionsDiary(Map<String, List<ActionDiaryVM>> monthActionDiary);
    }

    interface Presenter {
        void downloadActionsDiary(String day);
        void downloadActionsDiaryForMonth(String yearAndMonth);
    }

}
