package com.codernauti.sweetie.actions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codernauti.sweetie.R;
import com.codernauti.sweetie.actions.ActionsContract.DialogView;
import com.codernauti.sweetie.todolist.ToDoListActivity;
import com.codernauti.sweetie.utils.SharedPrefKeys;
import com.codernauti.sweetie.utils.Utility;

import java.util.List;


public class ActionNewToDoListFragment extends DialogFragment implements DialogView {
    public static final String TAG = "ActionNewToDoListFragment";

    private static final String USER_POSITIVE_RESPONSE = "Ok";
    private static final String USER_NEGATIVE_RESPONSE = "Cancel";

    private ActionsContract.Presenter mPresenter;

    private EditText mTitleToDoListEditText;

    static ActionNewToDoListFragment newInstance() {
        ActionNewToDoListFragment fragment = new ActionNewToDoListFragment();
        return fragment;
    }

    @Override
    public void setPresenter(ActionsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String dialogTitle = getString(R.string.action_new_todolist_title_dialog);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogLayout = inflater.inflate(R.layout.action_new_todolist_dialog, null);
        mTitleToDoListEditText = (EditText) dialogLayout.findViewById(R.id.action_new_todolist_title);

        return new AlertDialog.Builder(getActivity())
                .setView(dialogLayout)
                .setTitle(dialogTitle)
                .setPositiveButton(USER_POSITIVE_RESPONSE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputChatTitle = mTitleToDoListEditText.getText().toString().trim();

                        if (!userInputChatTitle.isEmpty()) {
                            String userName = Utility.getStringPreference(getActivity(), SharedPrefKeys.USER_UID);
                            List<String> keys = mPresenter.addAction(userInputChatTitle, userName, ActionVM.TODOLIST);

                            if (keys != null) {
                                Intent intent = new Intent(getActivity(), ToDoListActivity.class);
                                intent.putExtra(ToDoListActivity.TODOLIST_TITLE, userInputChatTitle);
                                intent.putExtra(ToDoListActivity.TODOLIST_DATABASE_KEY, keys.get(0));
                                intent.putExtra(ToDoListActivity.ACTION_DATABASE_KEY, keys.get(0));
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
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
