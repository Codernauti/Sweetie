package com.sweetcompany.sweetie.Actions;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.Firebase.FirebaseController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Eduard on 10/05/2017.
 */

public class ActionsPresenter implements ActionsContract.Presenter {

    public static final String TAG = "Action.presenter" ;

    private ActionsContract.View mView;
    private final FirebaseController mFireBaseController = FirebaseController.getInstance();

    private List<ActionVM> mActionsList = new ArrayList<>();


    public ActionsPresenter(ActionsContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        attachDataChange();
    }

    @Override
    public void pause() {
        //TODO implements and call detachDataChange()
    }

    // Clear actions, retrieve all actions on server
    private void updateActionsList(List<ActionFB> actionsFB) {
        ActionVM newActionVM;
        mActionsList.clear();

        for(ActionFB action : actionsFB){
            // TODO: use a Factory Method
            // for example use ActionConverter.convertToViewModel(action);
            switch (action.getType()) {
                case 0:
                    newActionVM = new ActionChatVM(action.getTitle(), action.getDescription(), action.getDataTime());
                    mActionsList.add(newActionVM);
                    break;
                case 1:
                    newActionVM = new ActionPhotoVM(action.getTitle(), action.getDescription());
                    mActionsList.add(newActionVM);
                    break;
            }
        }
        mView.updateActionsList(mActionsList);
    }

    // Push ActionChat on Server
    public void addActionChat(ActionFB newAction){
        DatabaseReference newActionRef = mFireBaseController.getDatabaseActionsReferences().push();
        newActionRef.setValue(newAction);
    }

    public void attachDataChange() {
        // Add value event listener to the post
        final List<ActionFB> actions = new ArrayList<>();

        ValueEventListener actionsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot actionSnapshot: dataSnapshot.getChildren()) {
                    ActionFB action = actionSnapshot.getValue(ActionFB.class);
                    actions.add(action);
                }

                if(dataSnapshot!=null) {
                    updateActionsList(actions);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase ;)", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mFireBaseController.getDatabaseActionsReferences().addValueEventListener(actionsListener);
    }
}
