package com.sweetcompany.sweetie.actions;


import android.util.Log;

import com.sweetcompany.sweetie.firebase.ActionFB;
import com.sweetcompany.sweetie.firebase.FirebaseActionsController;
import com.sweetcompany.sweetie.Utils.DataMaker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard on 10/05/2017.
 */

public class ActionsPresenter implements ActionsContract.Presenter,
                                        FirebaseActionsController.OnFirebaseActionsDataChange {

    public static final String TAG = "Action.presenter" ;

    private final ActionsContract.View mView;
    private final FirebaseActionsController mController;

    private List<ActionVM> mActionsList = new ArrayList<>();


    public ActionsPresenter(ActionsContract.View view, FirebaseActionsController controller) {
        mView = view;
        mView.setPresenter(this);
        mController = controller;
        mController.addListener(this);
    }

    @Override
    public void start() {
        mController.attachNetworkDatabase();
    }

    @Override
    public void pause() {
        mController.detachNetworkDatabase();
    }

    @Override
    public List<String> pushChatAction(String userInputChatTitle, String username) {
        ActionFB action = null;
        // TODO: add description and fix username variable, what username???
        try {
            action = new ActionFB(userInputChatTitle, username, "", DataMaker.get_UTC_DateTime(), ActionFB.CHAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (action != null) {
            return mController.pushChatAction(action, userInputChatTitle);
        }
        else {
            Log.d(TAG, "An error in the creation of a new ChatAction occurs!");
            return null;
        }
    }

    @Override
    public void pushAction(String userInputGalleryTitle, String username) {
        ActionFB action = null;
        try {
            action = new ActionFB(userInputGalleryTitle, username, "", DataMaker.get_UTC_DateTime(), ActionFB.PHOTO);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mController.pushAction(action);
    }

    // Controller callbacks

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
                            action.getDataTime(), action.getType(), action.getChildKey(), action.getKey());
                    mActionsList.add(newActionVM);
                    break;
                case 1:
                    newActionVM = new ActionGalleryVM(action.getTitle(), action.getDescription(), action.getDataTime(), action.getType());
                    mActionsList.add(newActionVM);
                    break;
            }
        }
        mView.updateActionsList(mActionsList);
    }

}