package com.sweetcompany.sweetie.Actions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard on 10/05/2017.
 */

public class ActionsPresenter implements ActionsContract.Presenter {

    private ActionsContract.View mView;

    private List<ActionVM> mActionsList = new ArrayList<>();


    public ActionsPresenter(ActionsContract.View view) {
        mView = view;
    }



    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    // Callback from database

    private boolean dirtyFlag = true;

    /**
     *  Test method
     */
    public void updateActionsList() {
        ActionVM newActionVM;

        if (dirtyFlag) {
            newActionVM = new ActionChatVM("ActionChatVM: " + String.valueOf(Math.random()));
            //TODO: decide if use Activity or Fragment
            //newActionVM.setView(mView);
            dirtyFlag = false;
        }
        else {
            newActionVM = new ActionPhotoVM("ActionPhotoVM: " + String.valueOf(Math.random()));
            //newActionVM.setView(mView);
            dirtyFlag = true;
        }

        mActionsList.add(newActionVM);
        mView.updateActionsList(mActionsList);
    }
}
