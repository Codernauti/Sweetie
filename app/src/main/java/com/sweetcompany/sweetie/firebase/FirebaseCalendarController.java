package com.sweetcompany.sweetie.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sweetcompany.sweetie.calendar.CalendarContract;
import com.sweetcompany.sweetie.calendar.CalendarPresenter;
import com.sweetcompany.sweetie.model.ActionDiaryFB;
import com.sweetcompany.sweetie.model.MessageFB;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eduard on 01-Aug-17.
 */

public class FirebaseCalendarController {

    private final DatabaseReference mCalendar;
    private ChildEventListener mDayListener;

    private CalendarControllerListener mListener;


    public interface CalendarControllerListener {
        void onActionDiaryAdded(ActionDiaryFB actionDiary);
    }


    public FirebaseCalendarController(String coupleUid) {
        mCalendar = FirebaseDatabase.getInstance()
                .getReference(Constraints.CALENDAR + "/" + coupleUid);
    }


    public void addListener(CalendarControllerListener listener) {
        mListener = listener;
    }

    public void attachListener(String day) {
        if (mDayListener == null) {
            mDayListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map<String, MessageFB> messages = new HashMap<>();

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        messages.put(data.getKey(), data.getValue(MessageFB.class));
                    }

                    ActionDiaryFB actionDiary = new ActionDiaryFB();
                    actionDiary.setKey(dataSnapshot.getKey());
                    actionDiary.setMessages(messages);

                    if (mListener != null) {
                        mListener.onActionDiaryAdded(actionDiary);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mCalendar.child(day).addChildEventListener(mDayListener);
        }

    }

    public void detachListener() {
        if (mDayListener != null) {
            mCalendar.removeEventListener(mDayListener);
        }
        mDayListener = null;
    }
}
