package com.sweetcompany.sweetie.todolist;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 09/08/2017.
 */

public class ToDoListAdapter extends RecyclerView.Adapter<CheckEntryViewHolder> implements CheckEntryViewHolder.OnViewHolderClickListener{

    private static final String TAG = "ToDoListAdapter";

    interface ToDoListAdapterListener {
        void onCheckEntryClicked(CheckEntryVM checkEntry);
        void onCheckEntryUnfocused(CheckEntryVM checkEntry);
        void onCheckEntryRemove(String key, int vhPositionToFocus);
    }

    private List<CheckEntryVM> mCheckEntryList = new ArrayList<>();
    private ToDoListAdapterListener mListener;

    void setListener(ToDoListAdapterListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return mCheckEntryList.get(position).getIdView();
    }

    @Override
    public CheckEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewToInflate = inflater.inflate(viewType, parent, false);
        CheckEntryViewHolder viewHolder;

        switch (viewType) {
            case R.layout.todolist_item:
                viewHolder = new CheckEntryViewHolder(viewToInflate);
                break;
            default:
                Log.w(TAG, "Error: no CheckEntry type match");
                // TODO: create a ErrorMessageViewHolder
                viewHolder = new CheckEntryViewHolder(viewToInflate);
                break;
        }
        Log.d(TAG, "onCreateViewHolder(): " + viewHolder.toString());
        viewHolder.setViewHolderClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CheckEntryViewHolder holder, final int position) {
        CheckEntryVM checkEntryVM = mCheckEntryList.get(position);
        checkEntryVM.configViewHolder(holder);
        Log.d(TAG, "onBindViewHolder(): " + checkEntryVM.getKey());
    }

    @Override
    public int getItemCount() {
        return mCheckEntryList.size();
    }

    void addCheckEntry(CheckEntryVM checkEntryVM) {
        mCheckEntryList.add(mCheckEntryList.size(),checkEntryVM);
        notifyItemInserted(mCheckEntryList.size()-1);
    }

    void removeCheckEntry(CheckEntryVM checkEntryVM) {
        int indexOldCheckEntry = searchIndexCheckEntryOf(checkEntryVM);
            mCheckEntryList.remove(indexOldCheckEntry);
            notifyItemRemoved(indexOldCheckEntry);
    }

    void changeCheckEntry(CheckEntryVM checkEntryVM) {
        int indexOldCheckEntry = searchIndexCheckEntryOf(checkEntryVM);
        if (indexOldCheckEntry != -1) {
            mCheckEntryList.set(indexOldCheckEntry, checkEntryVM);
            notifyItemChanged(indexOldCheckEntry);
        }
    }

    private int searchIndexCheckEntryOf(CheckEntryVM checkEntryVM) {
        String editCheckEntryKey = checkEntryVM.getKey();
        for (int i = 0; i < mCheckEntryList.size(); i++) {
            String checkEntryKey = mCheckEntryList.get(i).getKey();
            if (checkEntryKey.equals(editCheckEntryKey)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onCheckBoxClicked(int adapterPosition, boolean isChecked) {
        CheckEntryVM checkEntryVM = mCheckEntryList.get(adapterPosition);
        checkEntryVM.setChecked(isChecked);
        mListener.onCheckEntryClicked(checkEntryVM);
    }


    @Override
    public void onCheckEntryUnfocused(int adapterPosition, String text) {
        CheckEntryVM checkEntryVM = mCheckEntryList.get(adapterPosition);
        checkEntryVM.setText(text);
        mListener.onCheckEntryUnfocused(checkEntryVM);
    }

    @Override
    public void onCheckEntryRemove(int adapterPosition) {
        int vhPositionToFocus = -1;
        if (adapterPosition + 1 < mCheckEntryList.size()) {
            vhPositionToFocus = adapterPosition + 1;
        } else if (adapterPosition - 1 >= 0) {
            vhPositionToFocus = adapterPosition - 1;
        }
        String keyCheckEntryToRemove = mCheckEntryList.get(adapterPosition).getKey();
        mListener.onCheckEntryRemove(keyCheckEntryToRemove, vhPositionToFocus);
    }

}
