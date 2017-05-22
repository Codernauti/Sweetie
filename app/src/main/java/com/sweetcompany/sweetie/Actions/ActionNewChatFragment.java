package com.sweetcompany.sweetie.Actions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.sweetcompany.sweetie.Chat.ChatActivity;
import com.sweetcompany.sweetie.Firebase.ActionFB;
import com.sweetcompany.sweetie.Firebase.FirebaseActionsController;
import com.sweetcompany.sweetie.Firebase.FirebaseChatController;
import com.sweetcompany.sweetie.Firebase.FirebaseController;
import com.sweetcompany.sweetie.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Eduard on 13-May-17.
 */

// TODO: decide if use DialogFragment of this class or go to ChatsActivity
public class ActionNewChatFragment extends DialogFragment {

    private FirebaseController mFirebaseController = FirebaseController.getInstance();
    private FirebaseActionsController mFireBaseActionsController;

    public static final String TAG = "ActionNewChatFragment";

    static final String INPUT_CHAT_TITLE_KEY = "ChatTitle";

    private static final String USER_POSITIVE_RESPONSE = "Ok";
    private static final String USER_NEGATIVE_RESPONSE = "Cancel";

    private EditText mTitleChatEditText;

    static ActionNewChatFragment newInstance() {
        ActionNewChatFragment fragment = new ActionNewChatFragment();

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String dialogTitle = getString(R.string.action_new_chat_title_dialog);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogLayout = inflater.inflate(R.layout.action_new_chat_dialog, null);
        mTitleChatEditText = (EditText) dialogLayout.findViewById(R.id.action_new_chat_title);

        mFireBaseActionsController = FirebaseActionsController.getInstance();

        return new AlertDialog.Builder(getActivity())
                //.setIcon(R.drawable.alert_dialog_icon)
                .setView(dialogLayout)
                .setTitle(dialogTitle)
                .setPositiveButton(USER_POSITIVE_RESPONSE,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String userInputChatTitle = mTitleChatEditText.getText().toString();

                                if (!userInputChatTitle.isEmpty()) {

                                    DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
                                    String date = df.format(Calendar.getInstance().getTime());
                                    ActionFB action = new ActionFB(userInputChatTitle, mFirebaseController.getFirebaseUser().getDisplayName(), "desc...", date, ActionFB.CHAT);
                                    mFireBaseActionsController.pushActions(action);


                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra(INPUT_CHAT_TITLE_KEY, userInputChatTitle);

                                    startActivity(intent);
                                }
                            }
                        }
                )
                .setNegativeButton(USER_NEGATIVE_RESPONSE,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .create();

    }

}
