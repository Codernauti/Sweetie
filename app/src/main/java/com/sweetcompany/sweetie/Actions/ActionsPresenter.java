package com.sweetcompany.sweetie.Actions;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.FirebaseController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eduard on 10/05/2017.
 */

public class ActionsPresenter implements ActionsContract.Presenter {

    public static final String TAG = "Action.presenter" ;

    private final FirebaseController mFireBaseController = FirebaseController.getInstance();

    private ActionsContract.View mView;

    private List<ActionVM> mActionsList = new ArrayList<>();

    private DateFormat df = new SimpleDateFormat("dd/MM HH:mm");


    public ActionsPresenter(ActionsContract.View view) {
        mView = view;
    }


    @Override
    public void start() {
        mFireBaseController.attachDataChange();
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
            //newActionVM = new ActionChatVM("ActionChatVM: " + String.valueOf(Math.random()), "descrizione");
            DatabaseReference newActionRef = mFireBaseController.getDatabaseActionsReferences().push();
            String date = df.format(Calendar.getInstance().getTime());
            ActionChat action = new ActionChat("ActionChat: " + date, "heila Jesaaass!", date);
            newActionRef.setValue(action);

            //TODO: decide if use Activity or Fragment
            //newActionVM.setView(mView);
            dirtyFlag = false;
        }
        else {
            //newActionVM = new ActionPhotoVM("ActionPhotoVM: " + String.valueOf(Math.random()));
            //newActionVM.setView(mView);
            DatabaseReference newActionRef = mFireBaseController.getDatabaseActionsReferences().push();
            String date = df.format(Calendar.getInstance().getTime());
            newActionRef.setValue(new ActionPhoto("ActionPhoto: " + date, "Barabba ha aggiunto 5 foto della croce", date));
            dirtyFlag = true;
        }

        //mActionsList.add(newActionVM);
        mView.updateActionsList(mActionsList);

    }
}
