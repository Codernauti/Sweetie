package com.sweetcompany.sweetie.Chat;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 16/05/2017.
 */

class ChatAdapter extends RecyclerView.Adapter<MessageViewHolder>
        implements MessageViewHolder.OnViewHolderClickListener {

    private static final String TAG = "ChatAdapter";

    interface ChatAdapterListener {
        void onBookmarkClicked(MessageVM messageVM);
        //...
    }

    private List<MessageVM> mMessageList = new ArrayList<>();
    private ChatAdapterListener mListener;

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

    /**
     * Call when create ChatAdapter
     * @param listener
     */
    public void setChatAdapterListener(ChatAdapterListener listener) {
        mListener = listener;
    }

    /**
     *  Call when destroy ChatAdapterListener
     */
    public void removeChatAdapterListener() {
        mListener = null;
    }

    @Override
    public int getItemViewType(int position) {
        //TODO: this break the recycler of the viewHolder, the RecyclerView doesn't know the type
        return position;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        MessageVM message = mMessageList.get(position);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewToInflate = inflater.inflate(message.getIdView(), parent, false);
        MessageViewHolder viewHolder = message.newViewHolder(viewToInflate);
        viewHolder.setViewHolderClickListener(this);

        Log.d(TAG, "onCreateViewHolder(): " + message.getKey());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        MessageVM msgVM = mMessageList.get(position);
        msgVM.configViewHolder(holder);

        Log.d(TAG, "onBindViewHolder(): " + msgVM.getKey());
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


    /* Listener from ViewHolder */
    @Override
    public void onBookmarkClicked(int adapterPosition, boolean isBookmarked) {
        MessageVM msgToUpdate = mMessageList.get(adapterPosition);

        // Update MessageVM associate with ViewHolder
        msgToUpdate.setBookmarked(isBookmarked);

        // Notify fragment for the updating
        mListener.onBookmarkClicked(msgToUpdate);
    }
}
