package com.codernauti.sweetie.chat;

import android.util.Log;

import com.codernauti.sweetie.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateItemVM implements ChatItemVM {

    private static final SimpleDateFormat mFormat = new SimpleDateFormat("d MMMM yyyy");

    private Date mDate;

    DateItemVM(Date date) {
        mDate = date;
    }

    @Override
    public void configViewHolder(ChatViewHolder viewHolder) {
        DateViewHolder view = (DateViewHolder) viewHolder;
        view.setTextDate(mFormat.format(mDate));
    }

    @Override
    public int getIdView() {
        return R.layout.chat_date_list_item;
    }

    @Override
    public String getKey() {
        Log.w("DateItemVM", "Chat adapter ask the key of a Date Item");
        return mDate.toString();
    }
}
