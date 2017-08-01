package com.sweetcompany.sweetie.calendar;

/**
 * Created by Eduard on 01-Aug-17.
 */

public interface CalendarContract {

    interface View {
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void downloadActionsDiary(String day);
    }

}
