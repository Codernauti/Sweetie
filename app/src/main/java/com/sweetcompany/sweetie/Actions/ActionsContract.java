package com.sweetcompany.sweetie.Actions;

import java.util.List;

/**
 * Created by Eduard on 10/05/2017.
 */

public interface ActionsContract {

    interface View {
        void setPresenter(ActionsContract.Presenter presenter);
        void updateActionsList(List<ActionVM> actionsVM);

        void showCalendarFragment();
    }

    interface Presenter {
        void start();
        void pause();
        void updateActionsList();
    }
}
