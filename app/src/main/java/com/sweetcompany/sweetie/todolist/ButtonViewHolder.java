package com.sweetcompany.sweetie.todolist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.sweetcompany.sweetie.R;

/**
 * Created by lucas on 06/09/2017.
 */

public class ButtonViewHolder extends ToDoListViewHolder implements View.OnClickListener {

    Button mAddButton;
    OnButtonViewHolderClickListener mListener;
    public ButtonViewHolder(View itemView) {
        super(itemView);
        mAddButton = (Button) itemView.findViewById(R.id.todolist_add_button);
        mAddButton.setOnClickListener(this);
    }

    interface OnButtonViewHolderClickListener {
        void onAddButtonClicked();
    }
    void setButtonViewHolderClickListener(OnButtonViewHolderClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.todolist_add_button:
                mListener.onAddButtonClicked();
                break;
        }
    }
}
