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
import com.sweetcompany.sweetie.Firebase.FirebaseController;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.DataMaker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Eduard on 13-May-17.
 */

// TODO: decide if use DialogFragment of this class or go to ChatsActivity
public class ActionNewChatFragment extends DialogFragment {

    public static final String TAG = "ActionNewChatFragment";

    private static final String USER_POSITIVE_RESPONSE = "Ok";
    private static final String USER_NEGATIVE_RESPONSE = "Cancel";

    private EditText mTitleChatEditText;

    // TODO: NO, a Fragment mustn't have reference to firebase
    private FirebaseController mFirebaseController = FirebaseController.getInstance();
    private FirebaseActionsController mFireBaseActionsController;

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

                                    // TODO ripristinare getDisplayName()
                                    //ActionFB action = new ActionFB(userInputChatTitle, mFirebaseController.getFirebaseUser().getDisplayName(), "desc...", date, ActionFB.CHAT);

                                    ActionFB action = null;
                                    try {
                                        action = new ActionFB(userInputChatTitle, "Utente qualsiasi", "", DataMaker.get_UTC_DateTime(), ActionFB.CHAT);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    List<String> keys = mFireBaseActionsController.pushChatAction(action, userInputChatTitle); // [0] : chatKey, [1] : actionKey

                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra(ChatActivity.CHAT_TITLE, userInputChatTitle);
                                    intent.putExtra(ChatActivity.CHAT_DATABASE_KEY, keys.get(0));
                                    intent.putExtra(ChatActivity.ACTION_DATABASE_KEY, keys.get(1));

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
