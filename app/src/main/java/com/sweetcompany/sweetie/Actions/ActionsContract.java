package com.sweetcompany.sweetie.Actions;

import com.sweetcompany.sweetie.Firebase.ActionFB;

import java.util.List;

/**
 * Created by Eduard on 10/05/2017.
 */

public interface ActionsContract {

    interface View {
        void setPresenter(ActionsContract.Presenter presenter);
        void updateActionsList(List<ActionVM> actionsVM);
    }

    interface Presenter {
        void start();
        void pause();

        List<String> pushChatAction(String userInputChatTitle, String username);
        void pushAction(String userInputGalleryTitle, String username);
    }

}
