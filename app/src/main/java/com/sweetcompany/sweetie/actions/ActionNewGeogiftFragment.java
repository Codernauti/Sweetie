package com.sweetcompany.sweetie.actions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.geogift.GeogiftMakerActivity;
import com.sweetcompany.sweetie.utils.Utility;

import java.util.List;

/**
 * Created by ghiro on 07/08/2017.
 */

public class ActionNewGeogiftFragment extends DialogFragment implements ActionsContract.DialogView {

    public static final String TAG = "ActionNewGeogiftFragment";

    private static final String USER_POSITIVE_RESPONSE = "Ok";
    private static final String USER_NEGATIVE_RESPONSE = "Cancel";

    private EditText mTitleGeogiftEditText;

    private ActionsContract.Presenter mPresenter;

    static ActionNewGeogiftFragment newInstance() {
        ActionNewGeogiftFragment fragment = new ActionNewGeogiftFragment();
        return fragment;
    }

    @Override
    public void setPresenter(ActionsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String dialogTitle = getString(R.string.action_new_geogift_title_dialog);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogLayout = inflater.inflate(R.layout.action_new_geogift_dialog, null);
        mTitleGeogiftEditText = (EditText) dialogLayout.findViewById(R.id.action_new_geogift_title);

        return new AlertDialog.Builder(getActivity())
                //.setIcon(R.drawable.alert_dialog_icon)
                .setView(dialogLayout)
                .setTitle(dialogTitle)
                .setPositiveButton(USER_POSITIVE_RESPONSE,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String userInputGeogiftTitle = mTitleGeogiftEditText.getText().toString();

                                if (!userInputGeogiftTitle.isEmpty()) {
                                    // [0] : geogiftKey, [1] : actionKey
                                    String userName = Utility.getStringPreference(getActivity(), Utility.USER_UID);
                                    //List<String> keys = mPresenter.pushGeogiftAction(userInputGeogiftTitle, userName);

                                    //if (keys != null) {
                                        Intent intent = new Intent(getActivity(), GeogiftMakerActivity.class);
                                        intent.putExtra(GeogiftMakerActivity.GEOGIFT_TITLE, userInputGeogiftTitle);
                                        //intent.putExtra(GeogiftMakerActivity.GEOGIFT_DATABASE_KEY, keys.get(0));
                                        //intent.putExtra(GeogiftMakerActivity.ACTION_DATABASE_KEY, keys.get(1));
                                        startActivity(intent);
                                    //}
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