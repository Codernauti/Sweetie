package com.sweetcompany.sweetie.chat;

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
 * Created by ghiro on 16/05/2017.
 */

class ChatAdapter extends RecyclerView.Adapter<MessageViewHolder>
        implements MessageViewHolder.OnViewHolderClickListener {

    private static final String TAG = "ChatAdapter";

    interface ChatAdapterListener {
        void onBookmarkClicked(MessageVM messageVM);
    }

    private List<MessageVM> mMessageList = new ArrayList<>();
    private ChatAdapterListener mListener;

    /**
     * Call when create ChatAdapter
     * @param listener
     */
    void setChatAdapterListener(ChatAdapterListener listener) {
        mListener = listener;
    }

    /**
     *  Call when destroy ChatAdapterListener
     */
    void removeChatAdapterListener() {
        mListener = null;
    }

    @Override
    public int getItemViewType(int position) {
        //TODO: this break the recycler of the viewHolder, the RecyclerView doesn't know the type
        //return position;
        return mMessageList.get(position).getIdView();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //MessageVM message = mMessageList.get(viewType);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        View viewToInflate = inflater.inflate(viewType, parent, false);
        MessageViewHolder viewHolder;
        switch (viewType) {
            case R.layout.chat_user_list_item:
                viewHolder = new TextMessageViewHolder(viewToInflate, MessageVM.THE_MAIN_USER);
                break;
            case R.layout.chat_partner_list_item:
                viewHolder = new TextMessageViewHolder(viewToInflate, MessageVM.THE_PARTNER);
                break;
            default:
                Log.w(TAG, "Error: no MessageViewHolder type match");
                // TODO: create a ErrorMessageViewHolder
                viewHolder = new TextMessageViewHolder(viewToInflate, MessageVM.THE_PARTNER);
                break;
        }

        /*View viewToInflate = inflater.inflate(message.getIdView(), parent, false);
        MessageViewHolder viewHolder = message.newViewHolder(viewToInflate);*/
        viewHolder.setViewHolderClickListener(this);

        Log.d(TAG, "onCreateViewHolder(): " + viewHolder.toString());

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
        /*mMessageList.add(0, message);
        notifyItemInserted(0);*/
        mMessageList.add(message);
        notifyItemInserted(mMessageList.size() - 1);
    }

    void removeMessage(MessageVM msgVM) {
        int indexOldMessage = searchIndexMessageOf(msgVM);
        if (indexOldMessage != -1) {
            mMessageList.remove(indexOldMessage);
            notifyItemRemoved(indexOldMessage);
        }
    }

    void changeMessage(MessageVM msgVM) {
        int indexOldMessage = searchIndexMessageOf(msgVM);
        if (indexOldMessage != -1) {
            mMessageList.set(indexOldMessage, msgVM);
            notifyItemChanged(indexOldMessage);
        }
    }

    private int searchIndexMessageOf(MessageVM msg) {
        String modifyMsgKey = msg.getKey();
        for (int i = 0; i < mMessageList.size(); i++) {
            String msgKey = mMessageList.get(i).getKey();
            if (msgKey.equals(modifyMsgKey)) {
                return i;
            }
        }
        return -1;
    }

    void updateMessageList(List<MessageVM> messagesVM) {
        mMessageList.clear();
        mMessageList.addAll(messagesVM);
        Collections.reverse(mMessageList);
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
