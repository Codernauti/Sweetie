package com.codernauti.sweetie.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.codernauti.sweetie.model.CheckEntryFB;
import com.codernauti.sweetie.model.ToDoListFB;
import com.codernauti.sweetie.utils.DataMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FirebaseToDoListController extends FirebaseGeneralActionController {

    private static final String TAG = "FbToDoListController";

    private final String mActionUrl;
    private final String mToDoListUrl;              // todolist-checkentry/<couple_uid>/<todolist_uid>
    private final String mtoDoListCheckEntriesUrl;

    private final DatabaseReference mDatabaseRef;
    private final DatabaseReference mToDoListRef;
    private final DatabaseReference mToDoListCheckEntriesRef;

    private ValueEventListener mToDoListListener;
    private ChildEventListener mToDoListCheckEntriesListener;


    private List<Listener> mListeners = new ArrayList<>();

    public interface Listener {
        void onToDoListChanged(ToDoListFB todolist);

        void onCheckEntryAdded(CheckEntryFB checkEntry);
        void onCheckEntryRemoved(CheckEntryFB checkEntry);
        void onCheckEntryChanged(CheckEntryFB checkEntry);
    }

    public FirebaseToDoListController(String coupleUid, String toDoListUid,
                                      String userUid, String partnerUid){
        super(coupleUid, userUid, partnerUid, toDoListUid);

        mActionUrl = Constraints.ACTIONS + "/" + coupleUid + "/" + toDoListUid;
        mToDoListUrl = Constraints.TODOLIST + "/" + coupleUid + "/" + toDoListUid;
        mtoDoListCheckEntriesUrl = Constraints.TODOLIST_CHECKENTRY + "/" + coupleUid + "/" + toDoListUid;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mToDoListRef = mDatabaseRef.child(mToDoListUrl);
        mToDoListCheckEntriesRef = mDatabaseRef.child(mtoDoListCheckEntriesUrl);
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
            mToDoListCheckEntriesRef.addChildEventListener(mToDoListCheckEntriesListener);
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
            mToDoListCheckEntriesRef.removeEventListener(mToDoListCheckEntriesListener);
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
        updates.put(mtoDoListCheckEntriesUrl + "/" + chk.getKey(), chk);

        // update description and dataTime of action of this associated ToDoList
        updates.put(mActionUrl + "/" + Constraints.Actions.DESCRIPTION, chk.getText());
        updates.put(mActionUrl + "/" + Constraints.Actions.LAST_UPDATED_DATE, chk.getDateTime());

        mDatabaseRef.updateChildren(updates);

    }

    public void addCheckEntry(CheckEntryFB checkEntry) {
        Log.d(TAG, "Add CheckEntryFB: " + checkEntry);

        String checkEntryUid = mToDoListCheckEntriesRef.push().getKey();

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(mtoDoListCheckEntriesUrl + "/" + checkEntryUid, checkEntry);

        super.updateNotificationCounterAfterInsertion(updates, checkEntryUid);

        mDatabaseRef.updateChildren(updates);
    }

    public void removeCheckEntry(String checkEntryUid){
        Log.d(TAG, "Remove CheckEntryFB: " + checkEntryUid);
        // mToDoListCheckEntriesRef.child(checkEntryUid).removeValue();

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(mtoDoListCheckEntriesUrl + "/" + checkEntryUid, null);

        updates.put(mActionUrl + "/" + Constraints.Actions.DESCRIPTION, "");
        updates.put(mActionUrl + "/" + Constraints.Actions.LAST_UPDATED_DATE, DataMaker.get_UTC_DateTime());

        super.updateNotificationCounterAfterDeletion(updates, checkEntryUid);

        mDatabaseRef.updateChildren(updates);
    }

}
