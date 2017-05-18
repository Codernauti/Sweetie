package com.sweetcompany.sweetie.Actions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.FirebaseController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eduard on 10/05/2017.
 */

public class ActionsPresenter implements ActionsContract.Presenter {

    private final FirebaseController mFireBaseController = FirebaseController.getInstance();

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
            newActionVM = new ActionChatVM("ActionChatVM: " + String.valueOf(Math.random()), "descrizione");
            //Map<String, ActionVM> action = new HashMap<String, ActionVM>();
            //action.put("Barabbino CHAT", new ActionChatVM(String.valueOf(Math.random())));

            //mFireBaseController.getDatabaseActionsReferences().setValue(action);
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

    /*public void retrieveActions(){
        mFireBaseController.getDatabaseActionsReferences();


        // Attach a listener to read the data at our posts reference
        mFireBaseController.getDatabaseActionsReferences().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }*/
}
