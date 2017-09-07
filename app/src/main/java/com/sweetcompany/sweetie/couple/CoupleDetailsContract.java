package com.sweetcompany.sweetie.couple;

import android.net.Uri;

import java.util.Date;

/**
 * Created by Eduard on 20-Jul-17.
 */

public interface CoupleDetailsContract {

    interface View {
        void setPresenter(Presenter presenter);
        void updateCoupleData(String imageUri, String partnerOneName, String partnerTwoName,
                              Date anniversary, String anniversaryString, String creationTime);
        void updateUploadProgress(int progress);
        void hideUploadProgress();
    }

    interface Presenter {
        void deleteCouple();
        void sendCoupleImage(Uri stringUriLocal);
        void sendNewAnniversaryData(Date newAnniversary);
    }
}
