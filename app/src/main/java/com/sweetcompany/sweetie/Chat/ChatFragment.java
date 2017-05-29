package com.sweetcompany.sweetie.Chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sweetcompany.sweetie.R;

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

    private Toolbar mToolBar;
    private RecyclerView mChatListView;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageButton mPhotoPickerButton; // TODO: implement PhotoPicker
    private EditText mTextMessageInput;
    private Button mSendButton;

    private ChatAdapter mChatAdapter;
    private ChatContract.Presenter mPresenter;

    String actionKey;

    public static ChatFragment newInstance(Bundle bundle) {
        ChatFragment newChatFragment = new ChatFragment();
        newChatFragment.setArguments(bundle);

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

        String titleChat;
        titleChat = getArguments().getString(ChatActivity.CHAT_TITLE);
        Log.d(TAG, "from Intent CHAT_TITLE: " +
                getArguments().getString(ChatActivity.CHAT_TITLE));
        Log.d(TAG, "from Intent CHAT_DATABASE_KEY: " +
                getArguments().getString(ChatActivity.CHAT_DATABASE_KEY));

        mChatListView = (RecyclerView) root.findViewById(R.id.chat_list);

        mToolBar = (Toolbar) root.findViewById(R.id.my_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setTitle(titleChat);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

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
        mChatAdapter.updateMessageList(messagesVM);
    }

    @Override
    public void updateChatInfo(ChatVM chat) {
        mToolBar.setTitle(chat.getTitle());
    }

    @Override
    public void updateMessage(MessageVM msgVM) {
        mChatAdapter.addMessage(msgVM);
        mChatListView.scrollToPosition(mChatAdapter.getItemCount() - 1);
    }

    @Override
    public void removeMessage(MessageVM msgVM) {
        mChatAdapter.removeMessage(msgVM);
    }

    @Override
    public void changeMessage(MessageVM msgVM) {
        mChatAdapter.changeMessage(msgVM);
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

            // TODO: update view to feedback user if he is offline
            //mChatAdapter.addMessage(newMessage);

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
