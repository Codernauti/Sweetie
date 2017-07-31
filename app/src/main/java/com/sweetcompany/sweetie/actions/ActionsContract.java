package com.sweetcompany.sweetie.actions;

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
        List<String> pushChatAction(String userInputChatTitle, String username);
        List<String> pushGalleryAction(String userInputGalleryTitle, String username);
        //void pushAction(String userInputGalleryTitle, String username);
    }

    interface DialogView {
        void setPresenter(Presenter presenter);
    }
}
