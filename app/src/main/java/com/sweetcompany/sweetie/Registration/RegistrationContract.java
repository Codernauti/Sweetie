package com.sweetcompany.sweetie.Registration;

import com.sweetcompany.sweetie.Chat.ChatContract;

/**
 * Created by lucas on 16/05/2017.
 */

public interface RegistrationContract {
    interface View {
        void setPresenter(RegistrationContract.Presenter presenter);
    }
    interface Presenter {}
}

