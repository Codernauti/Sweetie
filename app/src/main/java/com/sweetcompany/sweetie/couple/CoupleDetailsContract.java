package com.sweetcompany.sweetie.couple;

import android.net.Uri;

/**
 * Created by Eduard on 20-Jul-17.
 */

public interface CoupleDetailsContract {

    interface View {
        void setPresenter(Presenter presenter);
        void updateCoupleData(String imageUri, String partnerOneName, String partnerTwoName, String anniversary, String creationTime);
    }

    interface Presenter {
        void deleteCouple();
        void sendCoupleImage(Uri stringUriLocal);
    }
}
