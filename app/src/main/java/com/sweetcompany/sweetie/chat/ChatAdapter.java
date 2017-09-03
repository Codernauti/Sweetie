package com.sweetcompany.sweetie.chat;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
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

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>
        implements MessageViewHolder.OnViewHolderClickListener {

    private static final String TAG = "ChatAdapter";


    private boolean chatDiary = false;  // dirty bit

    private List<ChatItemVM> mChatItemList = new ArrayList<>();
    private ChatAdapterListener mListener;

    // multiple items selection
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();
    private boolean mMultipleSelectionEnable = false;

    public interface ChatAdapterListener {
        void onBookmarkClicked(MessageVM messageVM, int type);
        void onPhotoClicked(TextPhotoMessageVM photoMessage);
        void onMessageLongClicked();
        void onMessageSelectionFinished();
    }

    /**
     * Call when create ChatAdapter or when destroy ChatAdapter, in this case pass null
     * @param listener
     */
    public void setListener(ChatAdapterListener listener) {
        mListener = listener;
    }

    /**
     *  Call from ChatDiary fragment for disable the bookmark option
     */
    public void setModeChatDiary() { chatDiary = true; }


    @Override
    public int getItemViewType(int position) {
        // use the unique id of the view for the type
        return mChatItemList.get(position).getIdView();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //MessageVM message = mChatItemList.get(viewType);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewToInflate = inflater.inflate(viewType, parent, false);
        ChatViewHolder viewHolder;
        switch (viewType) {

            case R.layout.chat_user_list_item_text:
            case R.layout.chat_partner_list_item_text:
                viewHolder = new TextMessageViewHolder(viewToInflate);
                ((MessageViewHolder) viewHolder).setViewHolderClickListener(this);

                if (chatDiary) {
                    ((MessageViewHolder) viewHolder).disableBookmarkButton();
                }
                break;

            case R.layout.chat_user_list_item_photo:
            case R.layout.chat_partner_list_item_photo:
                viewHolder = new TextPhotoMessageViewHolder(viewToInflate);
                ((MessageViewHolder) viewHolder).setViewHolderClickListener(this);

                if (chatDiary) {
                    ((MessageViewHolder) viewHolder).disableBookmarkButton();
                }
                break;

            case R.layout.chat_date_list_item:
                viewHolder = new DateViewHolder(viewToInflate);
                break;

            default:
                Log.w(TAG, "Error: no MessageViewHolder type match");
                // TODO: create a ErrorMessageViewHolder
                viewHolder = new TextMessageViewHolder(viewToInflate);
                break;
        }

        Log.d(TAG, "onCreateViewHolder(): " + viewHolder.toString());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatItemVM msgVM = mChatItemList.get(position);
        msgVM.configViewHolder(holder);

        if (msgVM instanceof MessageVM) {
            ((MessageViewHolder) holder).setViewHolderSelected(mSelectedItems.get(position, false));
        }
    }

    @Override
    public int getItemCount() {
        return mChatItemList.size();
    }


    void addDateItem(DateItemVM date) {
        mChatItemList.add(date);
        notifyItemInserted(mChatItemList.size() - 1);
    }

    public void addMessage(MessageVM message) {
        //TODO optimize change object field instead of remove
        if(searchIndexMessageOf(message) != -1){
            removeMessage(message);
        }
        mChatItemList.add(message);
        notifyItemInserted(mChatItemList.size() - 1);
    }

    void removeMessage(MessageVM msgVM) {
        int indexOldMessage = searchIndexMessageOf(msgVM);
        if (indexOldMessage != -1) {
            mChatItemList.remove(indexOldMessage);
            notifyItemRemoved(indexOldMessage);
        }
    }

    void changeMessage(MessageVM msgVM) {
        int indexOldMessage = searchIndexMessageOf(msgVM);
        if (indexOldMessage != -1) {
            mChatItemList.set(indexOldMessage, msgVM);
            notifyItemChanged(indexOldMessage);
        }
    }

    private int searchIndexMessageOf(MessageVM msg) {
        String modifyMsgKey = msg.getKey();
        for (int i = 0; i < mChatItemList.size(); i++) {
            String msgKey = mChatItemList.get(i).getKey();
            if (msgKey.equals(modifyMsgKey)) {
                return i;
            }
        }
        return -1;
    }

    public void removeMessage(String msgUid) {
        for (int i = 0; i < mChatItemList.size(); i++) {
            String msgKey = mChatItemList.get(i).getKey();
            if (msgKey.equals(msgUid)) {
                mChatItemList.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    @Deprecated
    void updateMessageList(List<MessageVM> messagesVM) {
        mChatItemList.clear();
        mChatItemList.addAll(messagesVM);
        Collections.reverse(mChatItemList);
        this.notifyDataSetChanged();
    }

    void updatePercentUpload(String msgUid, int perc){
        for (int indexMessageOf = 0; indexMessageOf < mChatItemList.size(); indexMessageOf++) {

            String msgKey = mChatItemList.get(indexMessageOf).getKey();

            if (msgKey.equals(msgUid)) {
                ((TextPhotoMessageVM) mChatItemList.get(indexMessageOf)).setPercent(perc);
                notifyItemChanged(indexMessageOf);
                return;
            }
        }
    }

    // selecting of items

    public void clearSelections() {
        mMultipleSelectionEnable = false;
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public List<MessageVM> getSelectedItems() {
        List<Integer> indexes = new ArrayList<>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); i++) {
            indexes.add(mSelectedItems.keyAt(i));
        }

        List<MessageVM> mediasSelected = new ArrayList<>();
        for (Integer index : indexes) {
            mediasSelected.add((MessageVM)mChatItemList.get(index));
        }
        return mediasSelected;
    }
    

    // Callbacks from ViewHolders

    @Override
    public void onBookmarkClicked(int adapterPosition, boolean isBookmarked, int type) {
        if (!mMultipleSelectionEnable) {
            // Only MessageViewHolder are listened, cast is secured
            MessageVM msgToUpdate = (MessageVM) mChatItemList.get(adapterPosition);

            // Update MessageVM associate with ViewHolder
            msgToUpdate.setBookmarked(isBookmarked);

            mListener.onBookmarkClicked(msgToUpdate, type);
        }
    }

    @Override
    public void onPhotoClicked(int adapterPosition) {
        // only TextPhotoMessage can have photoClicked triggered
        if (!mMultipleSelectionEnable) {
            mListener.onPhotoClicked((TextPhotoMessageVM) mChatItemList.get(adapterPosition));
        }
    }

    @Override
    public void onMessageClicked(int adapterPosition) {
        if (mMultipleSelectionEnable) {
            toggleSelection(adapterPosition);
        }

        if (mMultipleSelectionEnable && mSelectedItems.size() <= 0) {
            mListener.onMessageSelectionFinished();
        }
    }

    private void toggleSelection(int pos) {
        if (mSelectedItems.get(pos, false)) {
            mSelectedItems.delete(pos);
        } else {
            mSelectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    @Override
    public void onMessageLongClicked(int adapterPosition) {
        // only MessageViewHolder can have LongClicked triggered
        mListener.onMessageLongClicked();
        mMultipleSelectionEnable = true;

        toggleSelection(adapterPosition);
    }
}
