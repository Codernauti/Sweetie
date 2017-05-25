package com.sweetcompany.sweetie.Chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.Utils.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatFragment extends Fragment implements ChatContract.View, View.OnClickListener,
        ChatAdapter.ChatAdapterListener {

    private static final String TAG = "ChatFragment";

    private EditText mTextMessageInput;
    private ImageButton mPhotoPickerButton; // TODO: implement PhotoPicker
    private Button mSendButton;
    private RecyclerView mChatListView;
    private LinearLayoutManager mLinearLayoutManager;

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
        mChatAdapter.setChatAdapterListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chat_fragment, container, false);

        mChatListView = (RecyclerView) root.findViewById(R.id.chat_list);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setReverseLayout(true);

        mChatListView.setLayoutManager(mLinearLayoutManager);
        mChatListView.setAdapter(mChatAdapter);

        mTextMessageInput = (EditText) root.findViewById(R.id.chat_text_message_input);
        mPhotoPickerButton = (ImageButton) root.findViewById(R.id.chat_photo_picker_button);
        mSendButton = (Button) root.findViewById(R.id.chat_send_button);

        mSendButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.pause();
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateMessages(List<MessageVM> messagesVM) {
        mChatAdapter.updateActionsList(messagesVM);
    }


    // Callback bottom input bar
    @Override
    public void onClick(View v) {
        String inputText = mTextMessageInput.getText().toString();
        mTextMessageInput.setText("");

        if (!inputText.isEmpty()) {
            // TODO: is this responsibility of fragment?

            Date currentTime = Calendar.getInstance().getTime();
            // TODO: search correct time zone and change DataFormate based to android setting
            // check http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
            String stringCurrentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(currentTime);
            Log.d(TAG, "Current time: " + stringCurrentTime);

            MessageVM newMessage =
                    new TextMessageVM(inputText, MessageVM.THE_MAIN_USER, stringCurrentTime, false, null);

            // update view to feedback user if he is offline
            mChatAdapter.addMessage(newMessage);
            // TODO: smooth scroll is not good if user is upper of chat but scrollToPosition has a graphical bug
            //mChatListView.smoothScrollToPosition(mChatAdapter.getItemCount());
            mChatListView.smoothScrollToPosition(0);

            mPresenter.sendMessage(newMessage);
        }
    }

    // ChatAdapter callback
    @Override
    public void onBookmarkClicked(MessageVM messageVM) {
        // TODO: is this responsibility of fragment?
        mPresenter.bookmarkMessage(messageVM);
    }
}
