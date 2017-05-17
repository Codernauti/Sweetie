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

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static String TAG = "ChatAdapter";

    private List<TextMessageVM> mMessageList = new ArrayList<>();

    ChatAdapter(){
        mMessageList.add(new TextMessageVM("barabba", true));
        mMessageList.add(new TextMessageVM("Vogliamo BARABBA! vogliamo barabba!!!! BARABBA!", true));
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.chat_list_item, parent, false);
        ChatAdapter.ChatViewHolder viewHolder = new ChatAdapter.ChatViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        TextMessageVM textMessageVM = mMessageList.get(position);
        holder.mTextMessage.setText(textMessageVM.getText());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void newMessage(TextMessageVM message) {
        /*List<TextMessageVM> list = new ArrayList<>();
        list.add(message);

        mMessageList.clear();
        mMessageList.addAll(list);
*/
        mMessageList.add(message);
        notifyDataSetChanged();

        // Mostra solo il primo item
//        int oldSize = mMessageList.size();
//
//        mMessageList.add(oldSize, message);
//        notifyItemInserted(oldSize + 1);
    }



    class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextMessage;
        private ImageButton mBookmarkButton;

        public ChatViewHolder(View itemView) {
            super(itemView);

            mTextMessage = (TextView) itemView.findViewById(R.id.chat_item_text_view);
            mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_item_bookmark_button);

            mBookmarkButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mBookmarkButton.setSelected(!mBookmarkButton.isSelected());
        }
    }
}