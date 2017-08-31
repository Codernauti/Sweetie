package com.sweetcompany.sweetie.actions;

import android.util.Log;

import com.sweetcompany.sweetie.model.ActionFB;
import com.sweetcompany.sweetie.firebase.FirebaseActionsController;
import com.sweetcompany.sweetie.utils.DataMaker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard on 10/05/2017.
 */

public class ActionsPresenter implements ActionsContract.Presenter,
                                         FirebaseActionsController.OnFirebaseActionsDataChange {

    public static final String TAG = "ActionsPresenter" ;

    private final ActionsContract.View mView;
    private final FirebaseActionsController mController;

    private List<ActionVM> mActionsList = new ArrayList<>();
    private String mUserUid;

    public ActionsPresenter(ActionsContract.View view, FirebaseActionsController controller, String userUid) {
        mView = view;
        mView.setPresenter(this);
        mController = controller;
        mController.addListener(this);

        mUserUid = userUid;
    }

    @Override
    public List<String> addAction(String actionTitle, String username, int actionType) {
        // TODO: add mDescriptionTextView and fix username variable, what username???
        ActionFB action = new ActionFB(actionTitle, mUserUid, username, "", DataMaker.get_UTC_DateTime(), actionType);
        return mController.pushAction(action);
    }

    @Override
    public void removeAction(String actionUid, int actionChildType, String actionChildUid) {
        mController.removeAction(actionUid, actionChildType, actionChildUid);
    }

    // Controller callbacks

    // Clear actions, retrieve all actions on server
    @Override
    public void onActionsListChanged(List<ActionFB> actionsFB) {
        ActionVM newActionVM;
        mActionsList.clear();

        for(ActionFB action : actionsFB){
            // TODO: use a Factory Method
            // for example use ActionConverter.convertToViewModel(action);
            switch (action.getType()) {
                case ActionFB.CHAT:
                    newActionVM = new ActionChatVM(action.getTitle(), action.getDescription(),
                            action.getDataTime(), action.getType(), action.getChildKey(), action.getKey());
                    // TODO: test
                    newActionVM.setImageUrl(action.getImageUrl());
                    mActionsList.add(newActionVM);
                    break;

                case ActionFB.GALLERY:
                    newActionVM = new ActionGalleryVM(action.getTitle(), action.getDescription(),
                            action.getDataTime(), action.getType(), action.getChildKey(), action.getKey());
                    newActionVM.setImageUrl(action.getImageUrl());
                    mActionsList.add(newActionVM);
                    break;

                case ActionFB.TODOLIST:
                    newActionVM = new ActionToDoListVM(action.getTitle(), action.getDescription(),
                            action.getDataTime(), action.getType(), action.getChildKey(), action.getKey());
                    newActionVM.setImageUrl(action.getImageUrl());
                    mActionsList.add(newActionVM);
                    break;

                case ActionFB.GEOGIFT:
                    Log.d(TAG, "geogift finded!");
                    if(action.getUserCreator().equals(mUserUid) || action.getIsTriggered()) {
                        newActionVM = new ActionGeogiftVM(action.getTitle(), action.getDescription(),
                                action.getDataTime(), action.getType(), action.getChildKey(),
                                action.getKey(), action.getIsTriggered());
                        newActionVM.setImageUrl(action.getImageUrl());
                        mActionsList.add(newActionVM);
                    }
                    break;
            }
        }
        mView.updateActionsList(mActionsList);
    }

}
