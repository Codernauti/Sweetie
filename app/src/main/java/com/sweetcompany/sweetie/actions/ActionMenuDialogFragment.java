package com.sweetcompany.sweetie.actions;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

/**
 * Created by Eduard on 30-Aug-17.
 */

public class ActionMenuDialogFragment extends DialogFragment implements ActionsContract.DialogView {

    private static final String TAG = "ActionMenuDialogFrag";

    private static final String[] mItems = new String[2];
    private static final int REMOVE_OPT = 0;
    private static final int CHANGE_IMG_OPT = 1;

    static {
        mItems[REMOVE_OPT] = "Remove";
        mItems[CHANGE_IMG_OPT] = "Change image";
    }

    private static final String ACTION_UID_KEY = "actionUidKey";
    private static final String ACTION_CHILD_UID_KEY = "childUidKey";
    private static final String ACTION_CHILD_TYPE_KEY = "childTypeKey";

    private String mActionUid;
    private String mActionChildUid;
    private int mActionChildType;

    private ActionsContract.Presenter mPresenter;

    private ActionMenuDialogListener mListener;

    interface ActionMenuDialogListener {
        void onChangeActionImageSelected();
    }

    void setListener(ActionMenuDialogListener listener) { mListener = listener; }

    public static ActionMenuDialogFragment newInstance(String actionUid, int actionChildType,
                                                       String actionChildUid) {
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_UID_KEY, actionUid);
        bundle.putInt(ACTION_CHILD_TYPE_KEY, actionChildType);
        bundle.putString(ACTION_CHILD_UID_KEY, actionChildUid);

        ActionMenuDialogFragment fragment = new ActionMenuDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setPresenter(ActionsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {   // first opening
            savedInstanceState = getArguments();
        }

        if (savedInstanceState != null) {
            mActionUid = getArguments().getString(ACTION_UID_KEY);
            mActionChildUid = getArguments().getString(ACTION_CHILD_UID_KEY);
            mActionChildType = getArguments().getInt(ACTION_CHILD_TYPE_KEY);

            Log.d(TAG, mActionUid + " - " + mActionChildUid + " - " + mActionChildType);
        } else {
            Log.w(TAG, "Arguments not set!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setItems(mItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case REMOVE_OPT:
                                mPresenter.removeAction(mActionUid, mActionChildType, mActionChildUid);
                                break;

                            case CHANGE_IMG_OPT:
                                if (mActionChildType == ActionVM.CHAT) {
                                    mListener.onChangeActionImageSelected();
                                } else {
                                    Toast.makeText(getActivity(), "Feature not yet implemented", Toast.LENGTH_SHORT).show();
                                }
                                //mPresenter.addImageToAction(mActionUid, mActionChildType, mActionChildUid);
                                break;

                            default:
                                break;
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ACTION_UID_KEY, mActionUid);
        outState.putInt(ACTION_CHILD_TYPE_KEY, mActionChildType);
        outState.putString(ACTION_CHILD_UID_KEY, mActionChildUid);
    }
}
