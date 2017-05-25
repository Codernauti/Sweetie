package com.sweetcompany.sweetie.Registration;

import android.content.Context;

import com.sweetcompany.sweetie.Firebase.PairingRequest;

import java.util.List;

/**
 * Created by lucas on 16/05/2017.
 */

public interface RegisterContract {
    interface View {
        void setPresenter(RegisterContract.Presenter presenter);
        void updateRequest(List<PairingRequestVM> pairingRequestsVM);
        void notifyUsers(UserVM usersVM);
    }
    interface Presenter {
        void saveUserData(Context mContext);
        void savePairingRequest(PairingRequestVM pairingRequest);
        void attachUserDataListener(String orderByType, String equalsToData);
        void deletePairingRequest(String keyPairingRequest);
        void start();
        void pause();
    }
}

