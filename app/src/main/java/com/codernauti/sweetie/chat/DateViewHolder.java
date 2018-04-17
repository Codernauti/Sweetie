package com.codernauti.sweetie.chat;

import android.view.View;
import android.widget.TextView;

import com.codernauti.sweetie.R;


class DateViewHolder extends ChatViewHolder {

    private final TextView mDate;

    DateViewHolder(View viewToInflate) {
        super(viewToInflate);
        mDate = (TextView) viewToInflate.findViewById(R.id.chat_date_text_view);
    }

    void setTextDate(String date) {
        mDate.setText(date);
    }

}
