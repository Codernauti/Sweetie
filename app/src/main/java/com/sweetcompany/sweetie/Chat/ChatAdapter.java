package com.sweetcompany.sweetie.Chat;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private static final String TAG = "ChatAdapter";

    private List<TextMessageVM> mMessageList = new ArrayList<>();

    ChatAdapter(){

        // Populate items for TEST
        mMessageList.add(new TextMessageVM("Amore hai chiamato?", false));
        mMessageList.add(new TextMessageVM("Dammi 10 minuti e scendo", false));
        mMessageList.add(new TextMessageVM("Sono in macchina più avanti al palazzo", true));
        mMessageList.add(new TextMessageVM("Ok", true));
        mMessageList.add(new TextMessageVM(":) Scendi qualcosa di cibo?o magari da bere", true));
        mMessageList.add(new TextMessageVM("Se puoi", true));
        mMessageList.add(new TextMessageVM("Se vuoi torno", true));
        mMessageList.add(new TextMessageVM("Che cosa ha fatto di male?", true));
        mMessageList.add(new TextMessageVM("Buongiorno", false));
        mMessageList.add(new TextMessageVM("Hey! :)", true));
        mMessageList.add(new TextMessageVM("Allora...?", false));
        mMessageList.add(new TextMessageVM("Che c'è?", true));
    }

    @Override
    public int getItemViewType(int position) {
        return mMessageList.get(position).whoIs();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewToInflate;
        ChatAdapter.ChatViewHolder viewHolder;

        switch (viewType) {
            case TextMessageVM.THE_USER:
                viewToInflate = inflater.inflate(R.layout.chat_user_list_item, parent, false);
                viewHolder = new ChatAdapter.ChatViewHolder(viewToInflate, true);
                break;
            case TextMessageVM.THE_PARTNER:
                viewToInflate = inflater.inflate(R.layout.chat_partner_list_item, parent, false);
                viewHolder = new ChatAdapter.ChatViewHolder(viewToInflate, false);
                break;
            default:
                viewHolder = new ChatAdapter.ChatViewHolder(null, true);
                Log.d(TAG, "ChatViewHolder not correctly instantiate!");
        }

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

    public void addMessage(TextMessageVM message) {
        mMessageList.add(message);
        notifyItemInserted(mMessageList.size());
    }



    class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextMessage;
        private ImageButton mBookmarkButton;

        ChatViewHolder(View itemView, boolean isUser) {
            super(itemView);

            // TODO: da sistemare, non posso usare un booleano -> usare ViewHolder diverse
            if (isUser) {
                mTextMessage = (TextView) itemView.findViewById(R.id.chat_item_text_view);
                mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_item_bookmark_button);
            }
            else {  // isPartner
                mTextMessage = (TextView) itemView.findViewById(R.id.chat_partner_item_text_view);
                mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_partner_item_bookmark_button);
            }

            mBookmarkButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mBookmarkButton.setSelected(!mBookmarkButton.isSelected());
        }
    }
}
