package com.sweetcompany.sweetie.chatDiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.chat.ChatAdapter;
import com.sweetcompany.sweetie.chat.MessageVM;
import com.sweetcompany.sweetie.chat.TextPhotoMessageVM;

/**
 * Created by Eduard on 22-Aug-17.
 */

public class ChatDiaryFragment extends Fragment implements ChatDiaryContract.View,
    ChatAdapter.ChatAdapterListener {

    private ChatAdapter mChatAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mChatListView;
    private Toolbar mToolBar;

    private ChatDiaryContract.Presenter mPresenter;


    public static ChatDiaryFragment newInstance(Bundle bundle) {
        ChatDiaryFragment newChatFragment = new ChatDiaryFragment();
        newChatFragment.setArguments(bundle);

        return newChatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChatAdapter = new ChatAdapter();
        mChatAdapter.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.chat_diary_fragment, container, false);

        String titleChatDiary = getArguments().getString(ChatDiaryActivity.ACTION_DIARY_DATE);

        mToolBar = (Toolbar) root.findViewById(R.id.chat_diary_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleChatDiary);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(false);

        mChatListView = (RecyclerView) root.findViewById(R.id.chat_diary_list);
        mChatListView.setLayoutManager(mLinearLayoutManager);
        mChatListView.setAdapter(mChatAdapter);

        return root;
    }

    @Override
    public void setPresenter(ChatDiaryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateMessage(MessageVM msgVM) {
        mChatAdapter.addMessage(msgVM);
    }

    @Override
    public void removeMessage(String msgUid) {
        mChatAdapter.removeMessage(msgUid);
    }


    // ChatAdapter callbacks

    @Override
    public void onBookmarkClicked(MessageVM messageVM, int type) {

    }

    @Override
    public void onPhotoClicked(TextPhotoMessageVM photoMessage) {

    }
}
