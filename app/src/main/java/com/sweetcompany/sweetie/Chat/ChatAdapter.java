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

    private static String TAG = "ChatAdapter";

    private List<TextMessageVM> mMessageList = new ArrayList<>();

    ChatAdapter(){

        // Populate items for TEST
        mMessageList.add(new TextMessageVM("Sei tu il re dei Giudei?", true));
        mMessageList.add(new TextMessageVM("Tu lo dici.", false));
        mMessageList.add(new TextMessageVM("Non senti di quante cose ti accusano?", true));
        mMessageList.add(new TextMessageVM("Chi volete che sia lasciato libero: Barabba, oppure Gesù detto Cristo?", true));
        mMessageList.add(new TextMessageVM("Chi dei due volete che lasci libero?", true));
        mMessageList.add(new TextMessageVM("Barabba", false));
        mMessageList.add(new TextMessageVM("Che farò dunque di Gesù, detto Cristo?", true));
        mMessageList.add(new TextMessageVM("In croce!", false));
        mMessageList.add(new TextMessageVM("Che cosa ha fatto di male?", true));
        mMessageList.add(new TextMessageVM("In croce! in croce!", false));
        mMessageList.add(new TextMessageVM("Io non sono responsabile della morte di quest'uomo! Sono affari vostri!", true));
        mMessageList.add(new TextMessageVM("Il suo sangue ricada su di noi e sui nostri figli!", false));
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
        notifyDataSetChanged();
    }



    class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextMessage;
        private ImageButton mBookmarkButton;

        public ChatViewHolder(View itemView, boolean isUser) {
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
