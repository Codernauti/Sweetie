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

/**
 * Created by lucas on 04/08/2017.
 */

public class FirebaseToDoListController extends FirebaseGeneralActionController {

    private static final String TAG = "FbToDoListController";

    private final String mActionUrl;
    private final String mToDoListUrl;              // todolist-checkentry/<couple_uid>/<todolist_uid>
    private final String mtoDoListCheckEntriesUrl;

    private final DatabaseReference mDatabaseRef;
    private final DatabaseReference mToDoListRef;
    private final DatabaseReference mToDoListCheckEntryRef;

    private ValueEventListener mToDoListListener;
    private ChildEventListener mToDoListCheckEntriesListener;


    private List<Listener> mListeners = new ArrayList<>();

    public interface Listener {
        void onToDoListChanged(ToDoListFB todolist);

        void onCheckEntryAdded(CheckEntryFB checkEntry);
        void onCheckEntryRemoved(CheckEntryFB checkEntry);
        void onCheckEntryChanged(CheckEntryFB checkEntry);
    }

    public FirebaseToDoListController(String coupleUid, String toDoListKey, String actionUid,
                                      String userUid, String partnerUid){
        super(coupleUid, userUid, partnerUid, actionUid);

        mActionUrl = Constraints.ACTIONS + "/" + coupleUid + "/" + actionUid;
        mToDoListUrl = Constraints.TODOLIST + "/" + coupleUid + "/" + toDoListKey;
        mtoDoListCheckEntriesUrl = Constraints.TODOLIST_CHECKENTRY + "/" + coupleUid + "/" + toDoListKey;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mToDoListRef = mDatabaseRef.child(mToDoListUrl);
        mToDoListCheckEntryRef = mDatabaseRef.child(mtoDoListCheckEntriesUrl);
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    public void attachListeners() {
        super.attachListeners();

        if(mToDoListCheckEntriesListener == null){
            mToDoListCheckEntriesListener = new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    CheckEntryFB newCheckEntryFB = dataSnapshot.getValue(CheckEntryFB.class);
                    newCheckEntryFB.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onCheckEntryAdded to todo list: " + newCheckEntryFB.getText());

                    for(Listener listener: mListeners){
                        listener.onCheckEntryAdded(newCheckEntryFB);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    CheckEntryFB newCheckEntryFB = dataSnapshot.getValue(CheckEntryFB.class);
                    newCheckEntryFB.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onCheckEntryChanged to todo list: " + newCheckEntryFB.getText());

                    for(Listener listener: mListeners){
                        listener.onCheckEntryChanged(newCheckEntryFB);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    CheckEntryFB removedCheckEntry = dataSnapshot.getValue(CheckEntryFB.class);
                    removedCheckEntry.setKey(dataSnapshot.getKey());
                    Log.d(TAG, "onCheckEntryRemoved from todo list: " + removedCheckEntry.getText());

                    for (Listener listener : mListeners) {
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
            mToDoListCheckEntryRef.addChildEventListener(mToDoListCheckEntriesListener);
        }

        if (mToDoListListener == null) {
            mToDoListListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ToDoListFB toDoListFB = dataSnapshot.getValue(ToDoListFB.class);
                    toDoListFB.setKey(dataSnapshot.getKey());

                    for (Listener listener : mListeners) {
                        listener.onToDoListChanged(toDoListFB);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mToDoListRef.addValueEventListener(mToDoListListener);
        }
    }

    public void detachListeners() {
        super.detachListeners();

        if (mToDoListCheckEntriesListener != null) {
            mToDoListCheckEntryRef.removeEventListener(mToDoListCheckEntriesListener);
        }
        mToDoListCheckEntriesListener = null;

        if (mToDoListListener != null) {
            mToDoListRef.removeEventListener(mToDoListListener);
        }
        mToDoListListener = null;
    }

    public void updateCheckEntry(CheckEntryFB chk) {
        Log.d(TAG, "Update CheckEntryFB: " + chk);

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(mtoDoListCheckEntriesUrl + "/" + chk.getKey() + "/" + Constraints.CHECKED, chk.isChecked());
        updates.put(mtoDoListCheckEntriesUrl + "/" + chk.getKey() + "/" + Constraints.TEXT, chk.getText());

        // update description and dataTime of action of this associated ToDoList
        updates.put(mActionUrl + "/" + Constraints.Actions.DESCRIPTION, chk.getText());
        updates.put(mActionUrl + "/" + Constraints.Actions.DATE_TIME, chk.getDateTime());

        mDatabaseRef.updateChildren(updates);

    }

    public void addCheckEntry(CheckEntryFB checkEntry) {
        Log.d(TAG, "Add CheckEntryFB: " + checkEntry);

        String checkEntryUid = mToDoListCheckEntryRef.push().getKey();

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(mtoDoListCheckEntriesUrl + "/" + checkEntryUid, checkEntry);

        super.updateNotificationCounter(updates);

        mDatabaseRef.updateChildren(updates);
    }

    public void removeCheckEntry(String key){
        Log.d(TAG, "Remove CheckEntryFB: " + key);
        mToDoListCheckEntryRef.child(key).removeValue();
    }

}
