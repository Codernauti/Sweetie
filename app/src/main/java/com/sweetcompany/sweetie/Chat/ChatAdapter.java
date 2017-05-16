package com.sweetcompany.sweetie.Chat;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghiro on 16/05/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static String TAG = "ChatAdapter";

    private List<TextMessageVM> mMessageList = new ArrayList<>();

    ChatAdapter(){
        mMessageList.add(new TextMessageVM("barabba", true));
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
        holder.mText.setText(textMessageVM.getText());

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView mText;

        public ChatViewHolder(View itemView) {
            super(itemView);

            mText = (TextView) itemView.findViewById(R.id.chat_item_text_view);
        }
    }
}
