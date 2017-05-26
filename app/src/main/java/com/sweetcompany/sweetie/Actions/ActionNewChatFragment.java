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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Eduard on 13-May-17.
 */

// TODO: decide if use DialogFragment of this class or go to ChatsActivity
public class ActionNewChatFragment extends DialogFragment {

    public static final String TAG = "ActionNewChatFragment";

/*    public static final String DATABASE_CHAT_KEY = "DatabaseChatKey";
    public static final String INPUT_CHAT_TITLE_KEY = "InputChatTitle";*/

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

                                    // TODO this is a responsability for a Presenter
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String date = df.format(Calendar.getInstance().getTime());
                                    ActionFB action = new ActionFB(userInputChatTitle, mFirebaseController.getFirebaseUser().getDisplayName(), "desc...", date, ActionFB.CHAT);
                                    String chatKey = mFireBaseActionsController.pushChatAction(action, userInputChatTitle);


                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra(ChatActivity.CHAT_TITLE, userInputChatTitle);
                                    intent.putExtra(ChatActivity.CHAT_DATABASE_KEY, chatKey);

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
