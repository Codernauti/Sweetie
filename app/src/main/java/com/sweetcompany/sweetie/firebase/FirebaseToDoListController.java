package com.sweetcompany.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.model.CheckEntryFB;
import com.sweetcompany.sweetie.model.ToDoListFB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lucas on 04/08/2017.
 */

public class FirebaseToDoListController {
    private static final String TAG = "FbToDoListController";

    private final String mActionUid;

    private final String mToDoListUrl;              // todolist-checkentry/<couple_uid>/<todolist_uid>

    private final DatabaseReference mDatabase;
    private final DatabaseReference mToDoList;
    private final DatabaseReference mToDoListCheckEntry;
    private final DatabaseReference mAction;

    private ValueEventListener mToDoListListener;
    private ChildEventListener mToDoListCheckEntriesListener;

    private List<ToDoListControllerListener> mListeners = new ArrayList<>();

    public interface ToDoListControllerListener {
        void onToDoListChanged(ToDoListFB todolist);

        void onCheckEntryAdded(CheckEntryFB checkEntry);
        void onCheckEntryRemoved(CheckEntryFB checkEntry);
        void onCheckEntryChanged(CheckEntryFB checkEntry);
    }

    public FirebaseToDoListController(String coupleUid, String toDoListKey, String actionKey){
        mActionUid = actionKey;

        mToDoListUrl = Constraints.TODOLIST_CHECKENTRY + "/" + coupleUid + "/" + toDoListKey;

        FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
        mDatabase = firebaseDb.getReference();

        mToDoList = firebaseDb.getReference(Constraints.TODOLIST + "/" + coupleUid + "/" + toDoListKey);
        mToDoListCheckEntry = firebaseDb.getReference(Constraints.TODOLIST_CHECKENTRY + "/" + coupleUid + "/" + toDoListKey);
        mAction = firebaseDb.getReference(Constraints.ACTIONS + "/" + coupleUid + "/" + actionKey);
    }

    public void addListener(ToDoListControllerListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(ToDoListControllerListener listener) {
        mListeners.remove(listener);
    }

    public void attachListeners() {
        if(mToDoListCheckEntriesListener == null){
            mToDoListCheckEntriesListener = new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    CheckEntryFB newCheckEntryFB = dataSnapshot.getValue(CheckEntryFB.class);
                    newCheckEntryFB.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onCheckEntryAdded to todo list: " + newCheckEntryFB.getText());

                    for(ToDoListControllerListener listener: mListeners){
                        listener.onCheckEntryAdded(newCheckEntryFB);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    CheckEntryFB newCheckEntryFB = dataSnapshot.getValue(CheckEntryFB.class);
                    newCheckEntryFB.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onCheckEntryChanged to todo list: " + newCheckEntryFB.getText());

                    for(ToDoListControllerListener listener: mListeners){
                        listener.onCheckEntryChanged(newCheckEntryFB);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    CheckEntryFB removedCheckEntry = dataSnapshot.getValue(CheckEntryFB.class);
                    removedCheckEntry.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onCheckEntryRemoved from todo list: " + removedCheckEntry.getText());

                    for (ToDoListControllerListener listener : mListeners) {
                        listener.onCheckEntryRemoved(removedCheckEntry);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mToDoListCheckEntry.addChildEventListener(mToDoListCheckEntriesListener);
        }
    }

    public void detachListeners() {
        if (mToDoListListener != null) {
            mToDoList.removeEventListener(mToDoListListener);
        }
        mToDoListListener = null;

        if (mToDoListCheckEntriesListener != null) {
            mToDoListCheckEntry.removeEventListener(mToDoListCheckEntriesListener);
        }
        mToDoListCheckEntriesListener = null;
    }

    public void updateCheckEntry(CheckEntryFB chk) {
        Log.d(TAG, "Update CheckEntryFB: " + chk);

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(mToDoListUrl + "/" + chk.getKey() + "/" + Constraints.CHECKED, chk.isChecked());
        updates.put(mToDoListUrl + "/" + chk.getKey() + "/" + Constraints.TEXT, chk.getText());
        mDatabase.updateChildren(updates);
        // update description and dataTime of action of this associated ToDoList
        Map<String, Object> actionUpdates = new HashMap<>();
        actionUpdates.put("description", chk.getText());
        actionUpdates.put("dataTime", chk.getDateTime());
        mAction.updateChildren(actionUpdates);
    }

    public void addCheckEntry(CheckEntryFB chk) {
        Log.d(TAG, "Add CheckEntryFB: " + chk);
        // TODO: use atomic operation with hashmap

        // push a CheckEntry into mToDoListCheckEntry reference
        mToDoListCheckEntry.push().setValue(chk);
    }

    public void removeCheckEntry(String key){
        Log.d(TAG, "Remove CheckEntryFB: " + key);
        mToDoListCheckEntry.child(key).removeValue();
    }

}
