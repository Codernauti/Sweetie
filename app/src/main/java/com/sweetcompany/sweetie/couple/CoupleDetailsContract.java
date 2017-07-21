package com.sweetcompany.sweetie.couple;

/**
 * Created by Eduard on 20-Jul-17.
 */

public interface CoupleDetailsContract {

    interface View {
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void deleteCouple();
    }
}
