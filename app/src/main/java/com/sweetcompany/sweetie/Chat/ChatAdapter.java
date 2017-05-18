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

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.TextMessageViewHolder> {

    private static final String TAG = "ChatAdapter";

    private List<MessageVM> mMessageList = new ArrayList<>();

    ChatAdapter(){

        // Populate items for TEST
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
    }

    @Override
    public int getItemViewType(int position) {
        return mMessageList.get(position).getWho();
    }

    @Override
    public TextMessageViewHolder onCreateViewHolder(ViewGroup parent, int who) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewToInflate;
        TextMessageViewHolder viewHolder;

        if (who == TextMessageVM.THE_MAIN_USER) {
            viewToInflate = inflater.inflate(R.layout.chat_user_list_item, parent, false);
            viewHolder = new TextMessageViewHolder(viewToInflate, who);
        }
        else { // THE_PARTNER
            viewToInflate = inflater.inflate(R.layout.chat_partner_list_item, parent, false);
            viewHolder = new TextMessageViewHolder(viewToInflate, who);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TextMessageViewHolder holder, int position) {
        MessageVM msgVM = mMessageList.get(position);
        msgVM.configViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void addMessage(MessageVM message) {
        mMessageList.add(message);
        notifyItemInserted(mMessageList.size());
    }


    class TextMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextMessage;
        private ImageButton mBookmarkButton;

        TextMessageViewHolder(View itemView, int who) {
            super(itemView);

            if (who == MessageVM.THE_MAIN_USER) {
                mTextMessage = (TextView) itemView.findViewById(R.id.chat_item_text_view);
                mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_item_bookmark_button);
            }
            else {  // THE_PARTNER
                mTextMessage = (TextView) itemView.findViewById(R.id.chat_partner_item_text_view);
                mBookmarkButton = (ImageButton) itemView.findViewById(R.id.chat_partner_item_bookmark_button);
            }

            mBookmarkButton.setOnClickListener(this);
        }

        public void setText(String text) { mTextMessage.setText(text);}

        @Override
        public void onClick(View v) {
            mBookmarkButton.setSelected(!mBookmarkButton.isSelected());
        }
    }
}
