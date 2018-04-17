package com.codernauti.sweetie.actions;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codernauti.sweetie.R;
import com.theartofdev.edmodo.cropper.CropImage;


public class ActionMenuDialogFragment extends DialogFragment implements ActionsContract.DialogView {

    private static final String TAG = "ActionMenuDialogFrag";

    private static final String[] mItemsComplete = new String[2];
    private static final String[] mItemsMinor = new String[1];

    private static final int REMOVE_OPT = 0;
    private static final int CHANGE_IMG_OPT = 1;

    static {
        mItemsComplete[REMOVE_OPT] = "Remove";
        mItemsComplete[CHANGE_IMG_OPT] = "Change image";

        mItemsMinor[REMOVE_OPT] = "Remove";
    }

    private static final String ACTION_UID_KEY = "actionUidKey";
    private static final String ACTION_CHILD_UID_KEY = "childUidKey";
    private static final String ACTION_CHILD_TYPE_KEY = "childTypeKey";

    private String[] mItems;
    private String mActionUid;
    private String mActionChildUid;
    private int mActionChildType;

    private ActionsContract.Presenter mPresenter;

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

            /*if (mActionChildType == ActionVM.CHAT || mActionChildType == ActionVM.TODOLIST) {
                mItems = mItemsComplete;
            } else {*/
            //TODO
                mItems = mItemsMinor;
            /*}*/

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
                            case REMOVE_OPT: {
                                mPresenter.removeAction(mActionUid, mActionChildType, mActionChildUid);
                                break;
                            }
                            case CHANGE_IMG_OPT: {
                                final Fragment actionsFragment =
                                        ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                                                .findFragmentByTag("android:switcher:" + R.id.dashboard_pager + ":1");

                                CropImage.activity()
                                        .setAspectRatio(1, 1)
                                        .setMaxCropResultSize(1024, 1024)
                                        .start(actionsFragment.getContext(), actionsFragment);
                                break;
                            }
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
