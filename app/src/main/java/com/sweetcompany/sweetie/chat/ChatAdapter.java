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
        void onBookmarkClicked(MessageVM messageVM, int type);
        void onPhotoClicked(TextPhotoMessageVM photoMessage);
    }

    private List<MessageVM> mMessageList = new ArrayList<>();
    private ChatAdapterListener mListener;

    /**
     * Call when create ChatAdapter or when destroy ChatAdapter, in this case pass null
     * @param listener
     */
    void setChatAdapterListener(ChatAdapterListener listener) {
        mListener = listener;
    }


    @Override
    public int getItemViewType(int position) {
        // use the unique id of the view for the type
        return mMessageList.get(position).getIdView();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //MessageVM message = mMessageList.get(viewType);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewToInflate = inflater.inflate(viewType, parent, false);
        MessageViewHolder viewHolder;
        switch (viewType) {
            case R.layout.chat_user_list_item_text:
                viewHolder = new TextMessageViewHolder(viewToInflate, MessageVM.THE_MAIN_USER);
                break;
            case R.layout.chat_partner_list_item_text:
                viewHolder = new TextMessageViewHolder(viewToInflate, MessageVM.THE_PARTNER);
                break;
            case R.layout.chat_user_list_item_photo:
                viewHolder = new TextPhotoMessageViewHolder(viewToInflate, MessageVM.THE_MAIN_USER);
                break;
            case R.layout.chat_partner_list_item_photo:
                viewHolder = new TextPhotoMessageViewHolder(viewToInflate, MessageVM.THE_PARTNER);
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
        //TODO optimize change object field instead of remove
        if(searchIndexMessageOf(message)!=-1){
            removeMessage(message);
        }
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

    void updatePercentUpload(MessageVM mediaVM, int perc){
        int indexOldMedia = searchIndexMessageOf(mediaVM);
        if (indexOldMedia != -1) {
            mediaVM.setPercent(perc);
            mMessageList.set(indexOldMedia, mediaVM);
            notifyItemChanged(indexOldMedia);
        }
    }


    /* Listener from ViewHolder */
    @Override
    public void onBookmarkClicked(int adapterPosition, boolean isBookmarked, int type) {
        MessageVM msgToUpdate = mMessageList.get(adapterPosition);

        // Update MessageVM associate with ViewHolder
        msgToUpdate.setBookmarked(isBookmarked);

        // Notify fragment for the updating
        mListener.onBookmarkClicked(msgToUpdate, type);
    }

    @Override
    public void onPhotoClicked(int adapterPosition) {
        mListener.onPhotoClicked((TextPhotoMessageVM) mMessageList.get(adapterPosition));
    }
}
