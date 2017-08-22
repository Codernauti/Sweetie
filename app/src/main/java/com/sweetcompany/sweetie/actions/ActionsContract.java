package com.sweetcompany.sweetie.actions;

import com.sweetcompany.sweetie.geogift.GeoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard on 10/05/2017.
 */

public interface ActionsContract {

    interface View {
        void setPresenter(ActionsContract.Presenter presenter);
        void updateActionsList(List<ActionVM> actionsVM);
        void registerGeofence(GeoItem geoItem);
        void updateGeogiftList(ArrayList<String> geogiftNotVisitedKeys);
    }

    interface Presenter {
        List<String> pushChatAction(String userInputChatTitle, String username);
        List<String> pushGalleryAction(String userInputGalleryTitle, String username);
        List<String> pushToDoListAction(String userInputToDoListTitle, String username);
        void retrieveGeogift(String geoKey);
        //List<String> pushGeogiftAction(String userInputGeogiftTitle, String username);
        //void pushAction(String userInputGalleryTitle, String username);
    }

    interface DialogView {
        void setPresenter(Presenter presenter);
    }
}
