package com.sweetcompany.sweetie.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 16/05/2017.
 */

class ChatAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private static final String TAG = "ChatAdapter";

    private List<MessageVM> mMessageList = new ArrayList<>();

    ChatAdapter(){
        // Populate items for TEST
        /*for (int i = 0; i < 5000; i++) {
            mMessageList.add(new TextMessageVM("Amore hai chiamato?", MessageVM.THE_PARTNER));
            mMessageList.add(new TextMessageVM("Dammi 10 minuti e scendo", MessageVM.THE_PARTNER));
            mMessageList.add(new TextMessageVM("Sono in macchina più avanti al palazzo", MessageVM.THE_MAIN_USER));
            mMessageList.add(new TextMessageVM("Ok", MessageVM.THE_MAIN_USER));
            mMessageList.add(new TextMessageVM(":) Scendi qualcosa di cibo?o magari da bere", MessageVM.THE_MAIN_USER));
            mMessageList.add(new TextMessageVM("Se puoi", MessageVM.THE_MAIN_USER));
            mMessageList.add(new TextMessageVM("Se vuoi torno", MessageVM.THE_MAIN_USER));
            mMessageList.add(new TextMessageVM("Che cosa ha fatto di male?", MessageVM.THE_MAIN_USER));
            mMessageList.add(new TextMessageVM("Buongiorno", MessageVM.THE_PARTNER));
            mMessageList.add(new TextMessageVM("Hey! :)", MessageVM.THE_MAIN_USER));
            mMessageList.add(new TextMessageVM("Allora...?", MessageVM.THE_PARTNER));
            mMessageList.add(new TextMessageVM("Che c'è?", MessageVM.THE_MAIN_USER));
        }*/
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        MessageVM message = mMessageList.get(position);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewToInflate = inflater.inflate(message.getIdView(), parent, false);
        MessageViewHolder viewHolder = message.getViewHolder(viewToInflate);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        MessageVM msgVM = mMessageList.get(position);
        msgVM.configViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    void addMessage(MessageVM message) {
        mMessageList.add(message);
        notifyItemInserted(mMessageList.size());
    }

    void updateActionsList(List<MessageVM> messagesVM) {
        mMessageList.clear();
        mMessageList.addAll(messagesVM);
        this.notifyDataSetChanged();
    }
}
