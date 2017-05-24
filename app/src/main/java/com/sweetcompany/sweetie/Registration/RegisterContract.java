package com.sweetcompany.sweetie.Registration;

import android.content.Context;

import java.util.List;

/**
 * Created by lucas on 16/05/2017.
 */

public interface RegisterContract {
    interface View {
        void setPresenter(RegisterContract.Presenter presenter);
        void updateRequest(List<PairingRequestVM> pairingRequestsVM);
    }
    interface Presenter {
        void saveUserData(Context mContext);
        void start();
        void pause();
    }
}

