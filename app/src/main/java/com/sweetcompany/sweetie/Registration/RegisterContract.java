package com.sweetcompany.sweetie.Registration;

import android.content.Context;

import java.util.List;

/**
 * Created by lucas on 16/05/2017.
 */

interface RegisterContract {
    interface View {
        void setPresenter(RegisterContract.Presenter presenter);
        void updateRequest(List<PairingRequestVM> pairingRequestsVM);
        void notifyUser(UserVM usersVM);
        void notifyUserCheck(UserVM userVM);
    }

    interface Presenter {
        // StepOne
        void attachUserCheckListener(String key);
        // StepTwo
        void saveUserData(Context mContext);
    }

}

