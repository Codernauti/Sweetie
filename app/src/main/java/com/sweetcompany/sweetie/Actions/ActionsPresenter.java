package com.sweetcompany.sweetie.Actions;


import com.sweetcompany.sweetie.Firebase.ActionFB;
import com.sweetcompany.sweetie.Firebase.FirebaseActionsController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard on 10/05/2017.
 */

public class ActionsPresenter implements ActionsContract.Presenter, FirebaseActionsController.OnFirebaseActionsDataChange {

    public static final String TAG = "Action.presenter" ;

    private ActionsContract.View mView;
    FirebaseActionsController mFireBaseController;

    private List<ActionVM> mActionsList = new ArrayList<>();


    public ActionsPresenter(ActionsContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mFireBaseController = FirebaseActionsController.getInstance();
        mFireBaseController.addListener(this);
    }

    @Override
    public void start() {
        mFireBaseController.attachNetworkDatabase();
    }

    @Override
    public void pause() {
        mFireBaseController.detachNetworkDatabase();
    }

    // Clear actions, retrieve all actions on server
    public void updateActionsList(List<ActionFB> actionsFB) {
        ActionVM newActionVM;
        mActionsList.clear();

        for(ActionFB action : actionsFB){
            // TODO: use a Factory Method
            // for example use ActionConverter.convertToViewModel(action);
            switch (action.getType()) {
                case 0:
                    newActionVM = new ActionChatVM(action.getTitle(), action.getDescription(),
                            action.getTime(), action.getType(), action.getChildKey(), action.getA);
                    mActionsList.add(newActionVM);
                    break;
                case 1:
                    newActionVM = new ActionGalleryVM(action.getTitle(), action.getDescription(), action.getTime(), action.getType());
                    mActionsList.add(newActionVM);
                    break;
            }
        }
        mView.updateActionsList(mActionsList);
    }

}
