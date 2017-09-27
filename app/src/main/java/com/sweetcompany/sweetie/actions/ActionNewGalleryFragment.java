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
import android.widget.Toast;

import com.sweetcompany.sweetie.gallery.GalleryActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

import java.util.List;


// TODO: decide if use DialogFragment of this class or go to GalleryActivity
public class ActionNewGalleryFragment extends DialogFragment implements ActionsContract.DialogView {

    public static final String TAG = "ActionNewGalleryFragment";

    private static final String USER_POSITIVE_RESPONSE = "Ok";
    private static final String USER_NEGATIVE_RESPONSE = "Cancel";

    private EditText mTitleGalleryEditText;

    private ActionsContract.Presenter mPresenter;

    static ActionNewGalleryFragment newInstance() {
        ActionNewGalleryFragment fragment = new ActionNewGalleryFragment();
        return fragment;
    }

    @Override
    public void setPresenter(ActionsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String dialogTitle = getString(R.string.action_new_gallery_title_dialog);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogLayout = inflater.inflate(R.layout.action_new_gallery_dialog, null);
        mTitleGalleryEditText = (EditText) dialogLayout.findViewById(R.id.action_new_gallery_title);

        return new AlertDialog.Builder(getActivity())
                //.setIcon(R.drawable.alert_dialog_icon)
                .setView(dialogLayout)
                .setTitle(dialogTitle)
                .setPositiveButton(USER_POSITIVE_RESPONSE,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String userInputGalleryTitle = mTitleGalleryEditText.getText().toString().trim();

                                if (!userInputGalleryTitle.isEmpty()) {
                                    String userName = Utility.getStringPreference(getActivity(), SharedPrefKeys.USER_UID);
                                    List<String> keys = mPresenter.addAction(userInputGalleryTitle, userName, ActionVM.GALLERY);

                                    if (keys != null) {
                                        Intent intent = new Intent(getActivity(), GalleryActivity.class);
                                        intent.putExtra(GalleryActivity.GALLERY_TITLE, userInputGalleryTitle);
                                        intent.putExtra(GalleryActivity.ACTION_DATABASE_KEY, keys.get(0));
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
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
