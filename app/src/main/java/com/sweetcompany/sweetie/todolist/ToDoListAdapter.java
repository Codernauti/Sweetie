package com.sweetcompany.sweetie.todolist;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lucas on 09/08/2017.
 */

public class ToDoListAdapter extends ArrayAdapter<CheckEntryVM>{

    private static final String TAG = "ToDoListAdapter";

    public List<CheckEntryVM> mCheckEntryList = new ArrayList<>();

    private static class ViewHolder {
        CheckBox checkBox;
    }

    ToDoListAdapter(Context context, ArrayList<CheckEntryVM> checkEntries){
        super(context, R.layout.todolist_item, checkEntries);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckEntryVM checkEntry = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.todolist_item, parent, false);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkEntry);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkBox.setText(checkEntry.getText());
        viewHolder.checkBox.setChecked(checkEntry.isChecked());
        return convertView;
    }

    interface ToDoListAdapterListener {
        void onCheckEntryClicked(CheckEntryVM checkEntry);
        void onCheckEntryLongClicked(int position, List<CheckEntryVM> checkEntriesVM);
    }

    private ToDoListAdapterListener mListener;

    /**
     * Call when create ToDoListAdapter
     * @param listener
     */
    void setToDoListAdapterListener(ToDoListAdapterListener listener) {
        mListener = listener;
    }

    /**
     *  Call when destroy ToDoListAdapterListener
     */
    void removeToDoListAdapterListener() {
        mListener = null;
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
        if (indexOldCheckEntry != -1) {
            mCheckEntryList.remove(indexOldCheckEntry);
            notifyDataSetChanged();
        }
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
    public int getCount() {
        return mCheckEntryList.size();
    }
}
