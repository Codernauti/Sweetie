package com.sweetcompany.sweetie.todolist;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.List;


public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListViewHolder> implements CheckEntryViewHolder.OnCheckEntryViewHolderClickListener, ButtonViewHolder.OnButtonViewHolderClickListener{

    private static final String TAG = "ToDoListAdapter";

    private List<ToDoListItemVM> mToDoListItemList;
    private ToDoListAdapterListener mListener;

    ToDoListAdapter(){
        mToDoListItemList = new ArrayList<>();
        mToDoListItemList.add(new ToDoListButtonVM());
    }

    interface ToDoListAdapterListener {
        void onCheckEntryClicked(CheckEntryVM checkEntry);
        void onCheckEntryUnfocused(CheckEntryVM checkEntry);
        void onCheckEntryRemove(String key, int vhPositionToFocus);
        void onAddButtonClicked();
    }

    void setListener(ToDoListAdapterListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return mToDoListItemList.get(position).getIdView();
    }

    @Override
    public ToDoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewToInflate = inflater.inflate(viewType, parent, false);
        ToDoListViewHolder viewHolder;

        switch (viewType) {
            case R.layout.todolist_item:
                viewHolder = new CheckEntryViewHolder(viewToInflate);
                ((CheckEntryViewHolder) viewHolder).setCheckEntryViewHolderClickListener(this);
                break;
            case R.layout.todolist_button_item:
                viewHolder = new ButtonViewHolder(viewToInflate);
                ((ButtonViewHolder) viewHolder).setButtonViewHolderClickListener(this);
                break;
            default:
                Log.w(TAG, "Error: no CheckEntry type match");
                // TODO: create a ErrorMessageViewHolder
                viewHolder = new CheckEntryViewHolder(viewToInflate);
                break;
        }
        Log.d(TAG, "onCreateViewHolder(): " + viewHolder.toString());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ToDoListViewHolder holder, final int position) {
        ToDoListItemVM toDoListItemVM = mToDoListItemList.get(position);
        toDoListItemVM.configViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return mToDoListItemList.size();
    }


    void addCheckEntry(CheckEntryVM checkEntryVM) {
        mToDoListItemList.add(mToDoListItemList.size() - 1, checkEntryVM);
        notifyItemInserted(mToDoListItemList.size() - 2);
    }

    void removeCheckEntry(CheckEntryVM checkEntryVM) {
        int indexOldCheckEntry = searchIndexCheckEntryOf(checkEntryVM);
            mToDoListItemList.remove(indexOldCheckEntry);
            notifyItemRemoved(indexOldCheckEntry);
    }

    void changeCheckEntry(CheckEntryVM checkEntryVM) {
        int indexOldCheckEntry = searchIndexCheckEntryOf(checkEntryVM);
        if (indexOldCheckEntry != -1) {
            mToDoListItemList.set(indexOldCheckEntry, checkEntryVM);
            notifyItemChanged(indexOldCheckEntry);
        }
    }

    private int searchIndexCheckEntryOf(CheckEntryVM checkEntryVM) {
        String editCheckEntryKey = checkEntryVM.getKey();
        for (int i = 0; i < mToDoListItemList.size(); i++) {
            String checkEntryKey = ((CheckEntryVM) mToDoListItemList.get(i)).getKey();
            if (checkEntryKey.equals(editCheckEntryKey)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onCheckBoxClicked(int adapterPosition, boolean isChecked) {
        CheckEntryVM checkEntryVM = (CheckEntryVM) mToDoListItemList.get(adapterPosition);
        checkEntryVM.setChecked(isChecked);
        mListener.onCheckEntryClicked(checkEntryVM);
    }


    @Override
    public void onCheckEntryUnfocused(int adapterPosition, String text) {
        CheckEntryVM checkEntryVM = (CheckEntryVM) mToDoListItemList.get(adapterPosition);
        checkEntryVM.setText(text);
        mListener.onCheckEntryUnfocused(checkEntryVM);
    }

    @Override
    public void onCheckEntryRemove(int adapterPosition) {
        int vhPositionToFocus = -1;
        if (adapterPosition + 1 < mToDoListItemList.size() - 1) {
            vhPositionToFocus = adapterPosition + 1;
        } else if (adapterPosition - 1 >= 0) {
            vhPositionToFocus = adapterPosition - 1;
        }
        String keyCheckEntryToRemove = ((CheckEntryVM) mToDoListItemList.get(adapterPosition)).getKey();
        mListener.onCheckEntryRemove(keyCheckEntryToRemove, vhPositionToFocus);
    }

    @Override
    public void onEnterKeyPressed() {
        mListener.onAddButtonClicked();
    }

    @Override
    public void onAddButtonClicked() {
        mListener.onAddButtonClicked();
    }
}
