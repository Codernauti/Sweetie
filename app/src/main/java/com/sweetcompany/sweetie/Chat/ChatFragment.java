package com.sweetcompany.sweetie.Chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sweetcompany.sweetie.R;

import java.util.List;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatFragment extends Fragment implements ChatContract.View, View.OnClickListener {

    private EditText mTextMessageInput;
    private ImageButton mPhotoPickerButton;
    private Button mSendButton;
    private RecyclerView mChatListView;

    private ChatAdapter mChatAdapter;
    private ChatContract.Presenter mPresenter;

    public static ChatFragment newInstance() {
        ChatFragment newChatFragment = new ChatFragment();
        return newChatFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChatAdapter = new ChatAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chat_fragment, container, false);

        mChatListView = (RecyclerView) root.findViewById(R.id.chat_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mChatListView.setLayoutManager(layoutManager);
        mChatListView.setAdapter(mChatAdapter);

        mTextMessageInput = (EditText) root.findViewById(R.id.chat_text_message_input);
        mPhotoPickerButton = (ImageButton) root.findViewById(R.id.chat_photo_picker_button);
        mSendButton = (Button) root.findViewById(R.id.chat_send_button);

        mSendButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateMessages(List<TextMessageVM> messages) {
        //TODO: update the Adapter items list
    }

    @Override
    public void onClick(View v) {
        String inputText = mTextMessageInput.getText().toString();
        mTextMessageInput.setText("");

        if (!inputText.isEmpty()) {
            TextMessageVM newMessage = new TextMessageVM(inputText, true);

            mChatAdapter.newMessage(newMessage);

            //TODO: update also the presenter
            // mPresenter.sendMessage(...
        }

    }
}
