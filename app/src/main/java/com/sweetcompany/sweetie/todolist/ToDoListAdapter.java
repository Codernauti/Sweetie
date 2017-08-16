package com.sweetcompany.sweetie.todolist;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lucas on 09/08/2017.
 */

public class ToDoListAdapter extends RecyclerView.Adapter<CheckEntryViewHolder> implements CheckEntryViewHolder.OnViewHolderClickListener{

    private static final String TAG = "ToDoListAdapter";

    interface ToDoListAdapterListener {
        void onCheckEntryClicked(CheckEntryVM checkEntry);
        void onCheckEntryLongClicked(CheckEntryVM checkEntry);
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
    public void onBindViewHolder(CheckEntryViewHolder holder, int position) {
        CheckEntryVM checkEntryVM = mCheckEntryList.get(position);
        checkEntryVM.configViewHolder(holder);
        Log.d(TAG, "onBindViewHolder(): " + checkEntryVM.getKey());
    }

    @Override
    public int getItemCount() {
        return mCheckEntryList.size();
    }

    void addCheckEntry(CheckEntryVM checkEntryVM) {
        //TODO optimize change object field instead of remove
        if(searchIndexCheckEntryOf(checkEntryVM)!=-1){
            removeCheckEntry(checkEntryVM);
        }
        mCheckEntryList.add(checkEntryVM);
        notifyDataSetChanged();
    }

    void removeCheckEntry(CheckEntryVM checkEntryVM) {
        int indexOldCheckEntry = searchIndexCheckEntryOf(checkEntryVM);
            mCheckEntryList.remove(indexOldCheckEntry);
            notifyDataSetChanged();
    }

    void changeCheckEntry(CheckEntryVM checkEntryVM) {
        int indexOldCheckEntry = searchIndexCheckEntryOf(checkEntryVM);
        if (indexOldCheckEntry != -1) {
            mCheckEntryList.set(indexOldCheckEntry, checkEntryVM);
            notifyDataSetChanged();
        }
    }

    private int searchIndexCheckEntryOf(CheckEntryVM checkEntryVM) {
        String modifyCheckEntryKey = checkEntryVM.getKey();
        for (int i = 0; i < mCheckEntryList.size(); i++) {
            String checkEntryKey = mCheckEntryList.get(i).getKey();
            if (checkEntryKey.equals(modifyCheckEntryKey)) {
                return i;
            }
        }
        return -1;
    }

    void updateCheckEntriesList(List<CheckEntryVM> checkEntriesVM) {
        mCheckEntryList.clear();
        mCheckEntryList.addAll(checkEntriesVM);
        Collections.reverse(mCheckEntryList);
        notifyDataSetChanged();
    }

    @Override
    public void onCheckBoxClicked(int adapterPosition, boolean isChecked) {
        CheckEntryVM checkEntryVM = mCheckEntryList.get(adapterPosition);
        checkEntryVM.setChecked(isChecked);
        mListener.onCheckEntryClicked(checkEntryVM);
    }

    @Override
    public void onCheckBoxLongClicked(int adapterPosition) {
        CheckEntryVM checkEntryVM = mCheckEntryList.get(adapterPosition);
        mListener.onCheckEntryLongClicked(checkEntryVM);

    }
}
