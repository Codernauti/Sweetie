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

        // Populate items for TEST
        mMessageList.add(new TextMessageVM("Sei tu il re dei Giudei?", true));
        mMessageList.add(new TextMessageVM("Tu lo dici.", true));
        mMessageList.add(new TextMessageVM("Non senti di quante cose ti accusano?", true));
        mMessageList.add(new TextMessageVM("Chi volete che sia lasciato libero: Barabba, oppure Gesù detto Cristo?", true));
        mMessageList.add(new TextMessageVM("Chi dei due volete che lasci libero?", true));
        mMessageList.add(new TextMessageVM("Barabba", true));
        mMessageList.add(new TextMessageVM("Che farò dunque di Gesù, detto Cristo?", true));
        mMessageList.add(new TextMessageVM("In croce!", true));
        mMessageList.add(new TextMessageVM("Che cosa ha fatto di male?", true));
        mMessageList.add(new TextMessageVM("In croce! in croce!", true));
        mMessageList.add(new TextMessageVM("Io non sono responsabile della morte di quest'uomo! Sono affari vostri!", true));
        mMessageList.add(new TextMessageVM("Il suo sangue ricada su di noi e sui nostri figli!", true));
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
