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

import com.sweetcompany.sweetie.Firebase.ActionFB;
import com.sweetcompany.sweetie.Firebase.FirebaseActionsController;
import com.sweetcompany.sweetie.Firebase.FirebaseController;
import com.sweetcompany.sweetie.Gallery.GalleryActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.DataMaker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Federico Allegro on 24-May-17.
 */

// TODO: decide if use DialogFragment of this class or go to GalleryActivity
public class ActionNewGalleryFragment extends DialogFragment {

    private FirebaseController mFirebaseController = FirebaseController.getInstance();
    private FirebaseActionsController mFireBaseActionsController;

    public static final String TAG = "ActionNewGalleryFragment";

    static final String INPUT_GALLERY_TITLE_KEY = "GalleryTitle";

    private static final String USER_POSITIVE_RESPONSE = "Ok";
    private static final String USER_NEGATIVE_RESPONSE = "Cancel";

    private EditText mTitleGalleryEditText;

    static ActionNewGalleryFragment newInstance() {
        ActionNewGalleryFragment fragment = new ActionNewGalleryFragment();

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String dialogTitle = getString(R.string.action_new_gallery_title_dialog);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogLayout = inflater.inflate(R.layout.action_new_gallery_dialog, null);
        mTitleGalleryEditText = (EditText) dialogLayout.findViewById(R.id.action_new_gallery_title);

        mFireBaseActionsController = FirebaseActionsController.getInstance();

        return new AlertDialog.Builder(getActivity())
                //.setIcon(R.drawable.alert_dialog_icon)
                .setView(dialogLayout)
                .setTitle(dialogTitle)
                .setPositiveButton(USER_POSITIVE_RESPONSE,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String userInputGalleryTitle = mTitleGalleryEditText.getText().toString();

                                if (!userInputGalleryTitle.isEmpty()) {


                                    ActionFB action = null;
                                    try {
                                        action = new ActionFB(userInputGalleryTitle, mFirebaseController.getFirebaseUser().getDisplayName(), "desc...", DataMaker.get_UTC_DateTime(), ActionFB.PHOTO);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mFireBaseActionsController.pushAction(action);


                                    Intent intent = new Intent(getActivity(), GalleryActivity.class);
                                    intent.putExtra(INPUT_GALLERY_TITLE_KEY, userInputGalleryTitle);

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
