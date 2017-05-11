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

        //prepareActionsFake();
    }

    /**
     *  Test method
     */
    private void prepareActionsFake(){
        ActionVM actionVM = new ActionVM("Amore", "I love you my dear so much ma much much much", "08/05/17", "MSG");
        mActionsList.add(actionVM);

        actionVM = new ActionVM("Estate 2016", "Marina ha aggiunto 3 nuove foto", "02/04/07", "PHOTO");
        mActionsList.add(actionVM);

        actionVM = new ActionVM("FILM da vedere", "Marina ha aggiunto due elementi", "04/03/17", "TODO");
        mActionsList.add(actionVM);
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
