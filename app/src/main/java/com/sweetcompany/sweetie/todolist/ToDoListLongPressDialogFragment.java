package com.sweetcompany.sweetie.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.sweetcompany.sweetie.R;



/**
 * Created by lucas on 16/08/2017.
 */

public class ToDoListLongPressDialogFragment extends DialogFragment implements ToDoListContract.DialogView {
    public static final String TAG = "ToDoListLongPressDialogFragment";

    CheckEntryVM checkEntry;
    private ToDoListContract.Presenter mPresenter;

    static ToDoListLongPressDialogFragment newInstance(CheckEntryVM checkEntryVM) {
        ToDoListLongPressDialogFragment fragment = new ToDoListLongPressDialogFragment();
        fragment.setCheckEntry(checkEntryVM);
        return fragment;
    }

    public void setCheckEntry(CheckEntryVM checkEntry){
        this.checkEntry = checkEntry;
    }

    @Override
    public void setPresenter(ToDoListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] list = new String[2];
        list[0] = "Remove";
        list[1] = "Modify";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                mPresenter.removeCheckEntry(checkEntry);
                                break;
                            case 1:
                                Log.d(TAG, "Modify pressed");
                                break;
                            default:
                                Log.d(TAG, "Nothing pressed");
                                break;
                        }
                    }
                });
        return builder.create();
    }
}
